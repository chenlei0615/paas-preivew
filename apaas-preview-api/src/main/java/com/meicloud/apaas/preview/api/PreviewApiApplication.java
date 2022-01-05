package com.meicloud.apaas.preview.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class, scanBasePackages = "com.meicloud.apaas.preview")
public class PreviewApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PreviewApiApplication.class, args);
    }
}
