package org.csu.metrics.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 */
@RestController
@CrossOrigin(origins = "*")
public class TestController {
    @RequestMapping("/test")
    public String test() {
        System.out.println("test");
        return "test";
    }
}
