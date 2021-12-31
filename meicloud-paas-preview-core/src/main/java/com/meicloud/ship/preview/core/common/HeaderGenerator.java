package com.meicloud.ship.preview.core.common;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * @author chenlei140
 * @className ResponseGenerator
 * @description 响应流生成器
 * @date 2021/12/22 9:07
 */
public class HeaderGenerator {

    private HeaderGenerator() {
    }

    public static HttpHeaders pdfHeader(String fileName) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fileName);
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_PDF_VALUE));
        return headers;
    }
    public static HttpHeaders htmlHeader(String fileName) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fileName);
        headers.setContentType(MediaType.valueOf(MediaType.TEXT_HTML_VALUE));
        return headers;
    }

}
