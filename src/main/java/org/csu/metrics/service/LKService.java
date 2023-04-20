package org.csu.metrics.service;

import org.apache.log4j.BasicConfigurator;
import org.csu.metrics.common.Constant;
import org.csu.metrics.core.CK;
import org.csu.metrics.core.CKNumber;
import org.csu.metrics.core.CKReport;
import org.csu.metrics.vm.LkVO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LKService {
    public List<CKNumber> start(List<File> targetFile) {
        List<CKNumber> finalResult = new ArrayList<>();
        BasicConfigurator.configure();
        for (File file : targetFile) {
            CKReport report = new CK().calculate(file.getAbsolutePath());
            for (CKNumber result : report.all()) {
                if (result.isError()) continue;
                finalResult.add(result);
            }
        }
        return finalResult;
    }

    public List<LkVO> handleRequest(MultipartFile[] files) {
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

    private List<LkVO> convertToVO(List<CKNumber> results) {
        List<LkVO> vos = new ArrayList<>();
        for (CKNumber result : results) {
            LkVO lkVO = new LkVO();
            lkVO.setName(result.getFile().split("###")[1]);
            lkVO.setClazz(result.getClassName());
            lkVO.setType(result.getType());
            lkVO.setCs(String.valueOf(result.getWmc()));
            lkVO.setNoo(String.valueOf(result.getRfc()));
            lkVO.setNoa(String.valueOf(result.getLcom()));
            lkVO.setSi(String.valueOf(result.getCbo()));
            vos.add(lkVO);
        }
        return vos;
    }
}
