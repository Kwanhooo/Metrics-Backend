package org.csu.metrics.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Clazz {
    private String id;
    private String name;
    private Clazz father;
    private final Set<Clazz> children = new HashSet<>();
    private final Set<Attribute> attributes = new HashSet<>();
    private final Set<Operation> operations = new HashSet<>();

    private int wmc;
    private int dit;
    private int noc;
    private int cbo;
}
