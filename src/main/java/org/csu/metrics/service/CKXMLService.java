package org.csu.metrics.service;

import org.csu.metrics.common.Constant;
import org.csu.metrics.domain.CKBean;
import org.csu.metrics.domain.Clazz;
import org.csu.metrics.util.MetricUtil;
import org.csu.metrics.vm.CkXmlItemVO;
import org.csu.metrics.vm.CkXmlVO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CKXMLService {
    /**
     * 传入文件列表，返回CKNumber列表
     *
     * @param targetFile 文件列表
     * @return CKNumber列表
     * @throws Exception 异常
     */
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

    /**
     * 传入文件列表，返回CkVO列表
     *
     * @param files 文件列表
     * @return CkVO列表
     */
    public List<CkXmlVO> handleMultiRequest(MultipartFile[] files) {
        List<CkXmlVO> result = new ArrayList<>();
        for (MultipartFile file : files) {
            CkXmlVO vo = handleRequest(file);
            result.add(vo);
        }
        return result;
    }

    /**
     * 传入文件，返回CkXmlVO
     *
     * @param file 文件
     * @return CkXmlVO
     */
    public CkXmlVO handleRequest(MultipartFile file) {
        List<CKBean> result;
        try {
            File targetFile = new File(Constant.UPLOAD_PATH,
                    UUID.randomUUID() + "###" + file.getOriginalFilename());
            file.transferTo(targetFile);
            result = start(targetFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<CkXmlItemVO> ckVOS = convertToVO(result);
        return new CkXmlVO(file.getOriginalFilename(), ckVOS);
    }

    /**
     * 将CKBean列表转换为CkXmlItemVO列表
     *
     * @param results CKBean列表
     * @return CkXmlItemVO列表
     */
    private List<CkXmlItemVO> convertToVO(List<CKBean> results) {
        List<CkXmlItemVO> vos = new ArrayList<>();
        for (CKBean result : results) {
            CkXmlItemVO ckVO = new CkXmlItemVO();
            ckVO.setCLASS(result.getClazz());
//            ckVO.setType(result.getType());
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
