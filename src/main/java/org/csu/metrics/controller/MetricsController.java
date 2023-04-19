package org.csu.metrics.controller;

import jakarta.annotation.Resource;
import org.csu.metrics.common.CommonResponse;
import org.csu.metrics.service.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/metrics")
public class MetricsController {
    @Resource
    private CKXMLService ckXMLService;

    @Resource
    private CKJavaService ckJavaService;

    @Resource
    private TraditionService traditionService;

    @Resource
    private LKService lkService;

    @Resource
    private ExtendService extendService;


    @PostMapping("/ck/xml")
    public CommonResponse<?> ckXML(MultipartFile file) {
        return CommonResponse.createForSuccess(ckXMLService.handleRequest(file));
    }

    @PostMapping("/ck/java")
    public CommonResponse<?> ckJava(MultipartFile[] files) {
        System.out.println(files[0].getOriginalFilename());
        return CommonResponse.createForSuccess(ckJavaService.handleRequest(files));
    }

    @PostMapping("/tradition")
    public CommonResponse<?> tradition(MultipartFile[] files) {
        return CommonResponse.createForSuccess(traditionService.handleRequest(files));
    }

    @PostMapping("/lk")
    public CommonResponse<?> lk(MultipartFile[] files) {
        return CommonResponse.createForSuccess(lkService.handleRequest(files));
    }

    @PostMapping("/extend")
    public CommonResponse<?> extend(MultipartFile[] files) {
        return CommonResponse.createForSuccess(extendService.handleRequest(files));
    }
}
