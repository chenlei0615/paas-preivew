package com.meicloud.ship.preview.core.config;

import org.jodconverter.local.office.LocalOfficeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Properties;

/**
 * @author chenlei140
 * @className OfficeManagerInstance
 * @description OfficeManagerInstance
 * @date 2021/12/1 14:34
 */
@Component
public class OfficeManagerInstance {
    @Autowired
    private Environment environment;

    public static LocalOfficeManager INSTANCE = null;

    public static synchronized LocalOfficeManager start() {
        officeManagerStart();
        return INSTANCE;
    }

    @PostConstruct
    private void init() {
        String[] portNumbers = environment.getProperty("jodconverter.local.port-numbers", "").split(",");
        int[] ports = new int[portNumbers.length];

        for (int i = 0; i < portNumbers.length; i++) {
            ports[i] = Integer.parseInt(portNumbers[i]);
        }

        LocalOfficeManager.Builder builder = LocalOfficeManager.builder().install();
        builder.officeHome(environment.getProperty("jodconverter.local.office-home", ""));
        builder.portNumbers(ports);
        builder.taskExecutionTimeout(Long.valueOf(Integer.parseInt(environment.getProperty("jodconverter.local.taskExecutionTimeoutMinutes", "")) * 1000 * 60));
        builder.taskQueueTimeout(Long.valueOf(Integer.parseInt(environment.getProperty("jodconverter.local.taskQueueTimeoutHours", "")) * 1000 * 60 * 60));

        INSTANCE = builder.build();
        officeManagerStart();
    }

    private static void officeManagerStart() {
        if (INSTANCE.isRunning()) {
            return;
        }

        try {
            INSTANCE.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
