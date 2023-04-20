package org.csu.metrics.service;

import org.csu.metrics.common.Constant;
import org.csu.metrics.domain.CKBean;
import org.csu.metrics.domain.Clazz;
import org.csu.metrics.util.MetricUtil;
import org.csu.metrics.vm.CkVO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class CKXMLService {
    public List<CKBean> start(File targetFile) throws Exception {
        List<CKBean> finalResult = new ArrayList<>();

        if (targetFile != null) {
            URI uri = targetFile.toURI();
            URL url;
            url = uri.toURL();
            MetricUtil metricUtil = new MetricUtil();
            List<Clazz> classes = metricUtil.metric(url);

            for (Clazz e : classes) {
                CKBean bean = new CKBean(targetFile.getName(), e.getName(),
                        "class", String.valueOf(e.getWmc()),
                        String.valueOf(e.getWmc()), "0",
                        String.valueOf(e.getCbo()), String.valueOf(e.getDit()),
                        String.valueOf(e.getNoc()));
                finalResult.add(bean);
            }
        }
        return finalResult;
    }

    public List<CkVO> handleRequest(MultipartFile file) {
        List<CKBean> result;
        try {
            File targetFile = new File(Constant.UPLOAD_PATH,
                    UUID.randomUUID() + Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".")));
            file.transferTo(targetFile);
            result = start(targetFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return convertToVO(result);
    }

    private List<CkVO> convertToVO(List<CKBean> results) {
        List<CkVO> vos = new ArrayList<>();
        for (CKBean result : results) {
            CkVO ckVO = new CkVO();
            ckVO.setName(result.getFile());
            ckVO.setClazz(result.getClazz());
            ckVO.setType(result.getType());
            ckVO.setWMC(String.valueOf(result.getWmc()));
            ckVO.setRFC(String.valueOf(result.getRfc()));
            ckVO.setLCOM(String.valueOf(result.getLcom()));
            ckVO.setCBO(String.valueOf(result.getCbo()));
            ckVO.setDIT(String.valueOf(result.getDit()));
            ckVO.setNOC(String.valueOf(result.getNoc()));
            vos.add(ckVO);
        }
        return vos;
    }
}
