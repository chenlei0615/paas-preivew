package com.meicloud.ship.preview.core.config;

import org.jodconverter.local.office.LocalOfficeManager;
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
    public static LocalOfficeManager INSTANCE = null;

    public static synchronized LocalOfficeManager start() {
        officeManagerStart();
        return INSTANCE;
    }

    @PostConstruct
    private void init() {
        try {
            Properties properties = PropertiesLoaderUtils.loadAllProperties("application.properties");
            String[] portNumbers = properties.getProperty("jodconverter.local.port-numbers", "").split(",");
            int[] ports = new int[portNumbers.length];

            for (int i = 0; i < portNumbers.length; i++) {
                ports[i] = Integer.parseInt(portNumbers[i]);
            }

            LocalOfficeManager.Builder builder = LocalOfficeManager.builder().install();
            builder.officeHome(properties.getProperty("jodconverter.local.office-home", ""));
            builder.portNumbers(ports);
            builder.taskExecutionTimeout(Long.valueOf(Integer.parseInt(properties.getProperty("jodconverter.local.taskExecutionTimeoutMinutes", "")) * 1000 * 60));
            builder.taskQueueTimeout(Long.valueOf(Integer.parseInt(properties.getProperty("jodconverter.local.taskQueueTimeoutHours", "")) * 1000 * 60 * 60));

            INSTANCE = builder.build();
            officeManagerStart();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
