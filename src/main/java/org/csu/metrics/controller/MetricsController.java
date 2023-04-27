package org.csu.metrics.controller;

import org.csu.metrics.common.CommonResponse;
import org.csu.metrics.service.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * 代码度量控制器
 * 包含本项目所有的代码度量方法
 *
 * @author kwanho
 */
@RestController
@RequestMapping("/metrics")
@CrossOrigin(origins = "*")
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
    public CommonResponse<?> ckXML(@RequestParam("file") MultipartFile[] files) {
        System.out.println("================  CK XML ===============");
        System.out.println(Arrays.toString(files));
        System.out.println("=====================================");
        return CommonResponse.createForSuccess(ckXMLService.handleMultiRequest(files));
    }

    @PostMapping("/ck/java")
    public CommonResponse<?> ckJava(@RequestParam("file") MultipartFile[] files) {
        System.out.println("================  CK  ===============");
        System.out.println(Arrays.toString(files));
        System.out.println("=====================================");
        return CommonResponse.createForSuccess(ckJavaService.handleRequest(files));
    }

    @PostMapping("/tradition")
    public CommonResponse<?> tradition(@RequestParam("file") MultipartFile[] files) {
        System.out.println("============  TRADITION  ============");
        System.out.println(Arrays.toString(files));
        System.out.println("=====================================");
        return CommonResponse.createForSuccess(traditionService.handleRequest(files));
    }

    @PostMapping("/lk")
    public CommonResponse<?> lk(@RequestParam("file") MultipartFile[] files) {
        System.out.println("================  LK  ==============");
        System.out.println(Arrays.toString(files));
        System.out.println("=====================================");
        return CommonResponse.createForSuccess(lkService.handleRequest(files));
    }

    @PostMapping("/extend")
    public CommonResponse<?> extend(@RequestParam("file") MultipartFile[] files) {
        System.out.println("==============  EXTEND  =============");
        System.out.println(Arrays.toString(files));
        System.out.println("=====================================");
        return CommonResponse.createForSuccess(extendService.handleRequest(files));
    }
}
