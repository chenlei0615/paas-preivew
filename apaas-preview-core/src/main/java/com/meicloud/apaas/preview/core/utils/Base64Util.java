package com.meicloud.apaas.preview.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Encoder;

public class Base64Util {
    private static final Logger log = LoggerFactory.getLogger(Base64Util.class);

    /**
     * 将文件转成base64 字符串
     *
     * @param path 文件路径
     * @return
     * @throws Exception
     */
    public static String encodeBase64File(String path) {
        byte[] buffer = new byte[] {};
        try {
            File file = new File(path);
            FileInputStream inputFile = new FileInputStream(file);
            buffer = new byte[(int)file.length()];
            inputFile.read(buffer);
            inputFile.close();
        } catch (FileNotFoundException e) {
            log.error("img file path error: {} ", e.getStackTrace());
        } catch (IOException e) {
            log.error(" read img file error: {} ", e.getStackTrace());
        }
        return new BASE64Encoder().encode(buffer);
    }
}
