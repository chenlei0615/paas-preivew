package com.meicloud.apaas.preview.api.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * @author chenlei140
 * @className ResponseGenerator
 * @description 响应流生成器
 * @date 2021/12/22 9:07
 */
public class HeaderGenerator {

    private HeaderGenerator() {}

    /**
     * 
     * @param fileName
     * @param mediaType
     * @return
     */
    public static HttpHeaders headers(String fileName, MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fileName);
        headers.setContentType(mediaType);
        return headers;
    }

    /**
     * default pdf
     * 
     * @param fileName
     * @return
     */
    public static HttpHeaders headers(String fileName) {
        return headers(fileName, MediaType.valueOf(MediaType.APPLICATION_PDF_VALUE));
    }

}
