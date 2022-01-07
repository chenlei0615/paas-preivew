package com.meicloud.apaas.preview.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class, scanBasePackages = "com.meicloud.apaas.preview")
@EnableScheduling
public class PreviewApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PreviewApiApplication.class, args);
    }
}
