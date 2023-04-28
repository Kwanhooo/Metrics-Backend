package org.csu.metrics.util;

import org.csu.metrics.domain.Attribute;
import org.csu.metrics.domain.Clazz;
import org.csu.metrics.domain.Operation;
import org.csu.metrics.domain.Relationship;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.*;

public class MetricUtil {
    private final Map<String, Clazz> clazzMap = new HashMap<>();
    private final Map<String, Relationship> relationshipMap = new HashMap<>();

    public List<Clazz> metric(URL url) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(url);

        findClasses(document);
        findRelationships(document);

        computeWmc();
        computeDit();
        computeNoc();
        computeCbo();
        return new ArrayList<>(clazzMap.values());
    }

    private void computeWmc() {
        clazzMap.forEach((key, clazz) -> clazz.setWmc(clazz.getOperations().size()));
    }

    private void computeDit() {
        clazzMap.forEach((key, clazz) -> {
            int count = 0;
            Clazz father = clazz.getFather();
            while (father != null) {
                count++;
                father = father.getFather();
            }
            clazz.setDit(count);
        });
    }

    private void computeNoc() {
        clazzMap.forEach((key, clazz) -> clazz.setNoc(clazz.getChildren().size()));
    }

    private void computeCbo() {
        relationshipMap.forEach((key, relationship) -> {
            if (relationship.getType() != Relationship.Type.GENERALIZATION) {
                int cbo = relationship.getClazz1().getCbo();
                relationship.getClazz1().setCbo(cbo + 1);

                cbo = relationship.getClazz2().getCbo();
                relationship.getClazz2().setCbo(cbo + 1);
            }
        });
    }

    private void findClasses(Document document) {
        Element rootElement = document.getRootElement();
        Element classesElement = rootElement.element("Classes");
        for (Element element : classesElement.elements()) {
            Clazz clazz = new Clazz();
            clazz.setId(element.attributeValue("Id"));
            clazz.setName(element.elementText("Name"));

            Set<Attribute> attributes = clazz.getAttributes();
            if(element.element("Attributes")!=null){
                for (Element attributeElement : element.element("Attributes").elements()) {
                    Attribute attribute = new Attribute();
                    attribute.setId(attributeElement.attributeValue("Id"));
                    attribute.setName(attributeElement.elementText("Name"));
                    attribute.setDataType(attributeElement.elementText("DataType"));
                    attribute.setVisibility(attributeElement.elementText("Attribute.Visibility"));
                    attributes.add(attribute);
                }
            }

            Set<Operation> operations = clazz.getOperations();
            if(element.element("Operations")!=null) {
                for (Element operationElement : element.element("Operations").elements()) {
                    Operation operation = new Operation();
                    operation.setId(operationElement.attributeValue("Id"));
                    operation.setName(operationElement.elementText("Name"));
                    operation.setReturnType(operationElement.elementText("ReturnType"));
                    operations.add(operation);
                }
            }

            clazzMap.put(clazz.getId(), clazz);
        }
    }

    private void findRelationships(Document document) {
        Element rootElement = document.getRootElement();

        Map<String, Relationship.Type> typeMap = new HashMap<>();
        typeMap.put("Generalizations", Relationship.Type.GENERALIZATION);
        typeMap.put("Associations", Relationship.Type.ASSOCIATION);
        typeMap.put("Aggregations", Relationship.Type.AGGREGATION);
        typeMap.put("Compositions", Relationship.Type.COMPOSITION);
        typeMap.put("Dependencies", Relationship.Type.DEPENDENCY);

        typeMap.forEach((key, type) -> {
            Element relationshipsElement = rootElement.element(key);
            if (relationshipsElement != null) {
                for (Element element : relationshipsElement.elements()) {
                    Relationship relationship = new Relationship();
                    relationship.setId(element.attributeValue("Id"));
                    relationship.setType(type);

                    Clazz clazz1 = clazzMap.get(element.element("Object1").element("Class").attributeValue("Ref"));
                    Clazz clazz2 = clazzMap.get(element.element("Object2").element("Class").attributeValue("Ref"));

                    if (type == Relationship.Type.GENERALIZATION) {
                        clazz2.setFather(clazz1);
                        clazz1.getChildren().add(clazz2);
                    }

                    relationship.setClazz1(clazz1);
                    relationship.setClazz2(clazz2);
                    relationshipMap.put(relationship.getId(), relationship);
                }
            }
        });
    }
}
