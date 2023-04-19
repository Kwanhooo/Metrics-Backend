package org.csu.metrics.domain;

import lombok.Data;

@Data
public class Attribute {
    private String id;
    private String name;
    private String dataType;
    private String visibility;
}