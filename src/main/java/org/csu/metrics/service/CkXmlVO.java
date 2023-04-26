package org.csu.metrics.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.csu.metrics.vm.CkXmlItemVO;

import java.util.List;

@Data
@AllArgsConstructor
public class CkXmlVO {
    private String name;
    private List<CkXmlItemVO> classes;
}

