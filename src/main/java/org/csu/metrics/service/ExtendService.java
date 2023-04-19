package org.csu.metrics.service;

import org.apache.log4j.BasicConfigurator;
import org.csu.metrics.common.Constant;
import org.csu.metrics.core.CK;
import org.csu.metrics.core.CKNumber;
import org.csu.metrics.core.CKReport;
import org.csu.metrics.vm.ExtendVO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ExtendService {
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

    public List<ExtendVO> handleRequest(MultipartFile[] files) {
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
        for (int i = 0; i < results.size(); i++) {
            results.get(i).setFile(targetFiles.get(i).getName().split("###")[1]);
        }
        return convertToVO(results);
    }

    private List<ExtendVO> convertToVO(List<CKNumber> results) {
        List<ExtendVO> vos = new ArrayList<>();
        for (CKNumber result : results) {
            ExtendVO vo = new ExtendVO();
            vo.setFile(result.getFile());
            vo.setNOM(String.valueOf(result.getNom()));
            vo.setNOPM(String.valueOf(result.getNopm()));
            vo.setNOSM(String.valueOf(result.getNosm()));
            vo.setNOF(String.valueOf(result.getNof()));
            vo.setNOPF(String.valueOf(result.getNopf()));
            vo.setNOSF(String.valueOf(result.getNosf()));
            vo.setNOSI(String.valueOf(result.getNosi()));
            vos.add(vo);
        }

        return vos;
    }
}
