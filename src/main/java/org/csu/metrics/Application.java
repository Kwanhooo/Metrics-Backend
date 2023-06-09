package org.csu.metrics;

import org.csu.metrics.common.Constant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        init();
    }

    /**
     * 初始化缓存文件夹
     */
    private static void init() {
        File file = new File(Constant.UPLOAD_PATH);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            System.out.println("创建`缓存`文件夹" + mkdirs + (mkdirs ? "成功" : "失败"));
        }
    }

}
