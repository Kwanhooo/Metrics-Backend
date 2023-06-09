package org.csu.metrics.service;

import org.csu.metrics.common.Constant;
import org.csu.metrics.core.CK;
import org.csu.metrics.core.CKNumber;
import org.csu.metrics.core.CKReport;
import org.csu.metrics.vm.CkVO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CKJavaService {
    /**
     * 传入文件列表，返回CKNumber列表
     *
     * @param targetFiles 文件列表
     * @return CKNumber列表
     */
    public List<CKNumber> start(List<File> targetFiles) {
        List<CKNumber> finalResult = new ArrayList<>();
        for (File file : targetFiles) {
            CKReport report = new CK().calculate(file.getAbsolutePath());
            for (CKNumber result : report.all()) {
                if (result.isError()) continue;
                finalResult.add(result);
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
    public List<CkVO> handleRequest(MultipartFile[] files) {
        List<File> targetFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            File targetFile = new File(Constant.UPLOAD_PATH,
                    UUID.randomUUID() + "###" + file.getOriginalFilename());
            try {
                file.transferTo(targetFile);
                targetFiles.add(targetFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        List<CKNumber> results = start(targetFiles);
        return convertToVO(results);
    }

    /**
     * 将CKNumber列表转换为CkVO列表
     *
     * @param results CKNumber列表
     * @return CkVO列表
     */
    private List<CkVO> convertToVO(List<CKNumber> results) {
        List<CkVO> vos = new ArrayList<>();
        for (CKNumber result : results) {
            CkVO ckVO = new CkVO();
            ckVO.setName(result.getFile().split("###")[1]);
            ckVO.setCLASS(result.getClassName());
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
