package org.csu.metrics.domain;

import lombok.Data;

@Data
public class Relationship {
    private String id;
    private Clazz clazz1;
    private Clazz clazz2;
    private Type type;

    public enum Type {
        GENERALIZATION,
        ASSOCIATION,
        AGGREGATION,
        COMPOSITION,
        DEPENDENCY
    }
}