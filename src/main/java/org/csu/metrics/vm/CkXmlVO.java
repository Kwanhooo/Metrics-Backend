package org.csu.metrics.vm;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CkXmlVO {
    private String name;
    private List<CkXmlItemVO> classes;
}

