package org.csu.metrics.domain;

import lombok.ToString;

@ToString
public class CKBean {
    String file;
    String clazz;
    String type;
    String wmc;
    String rfc;
    String lcom;
    String cbo;
    String dit;
    String noc;

    public CKBean(String file, String clazz, String type, String wmc, String rfc, String lcom, String cbo, String dit, String noc) {
        this.file = file;
        this.clazz = clazz;
        this.type = type;
        this.wmc = wmc;
        this.rfc = rfc;
        this.lcom = lcom;
        this.cbo = cbo;
        this.dit = dit;
        this.noc = noc;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWmc() {
        return wmc;
    }

    public void setWmc(String wmc) {
        this.wmc = wmc;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getLcom() {
        return lcom;
    }

    public void setLcom(String lcom) {
        this.lcom = lcom;
    }

    public String getCbo() {
        return cbo;
    }

    public void setCbo(String cbo) {
        this.cbo = cbo;
    }

    public String getDit() {
        return dit;
    }

    public void setDit(String dit) {
        this.dit = dit;
    }

    public String getNoc() {
        return noc;
    }

    public void setNoc(String noc) {
        this.noc = noc;
    }
}
