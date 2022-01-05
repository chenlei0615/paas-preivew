package com.meicloud.apaas.preview.core.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * @author chenlei140
 * @className ResponseGenerator
 * @description 响应流生成器
 * @date 2021/12/22 9:07
 */
public class HeaderGenerator {

  private static final String FILE_SEPARATOR = ".";

  private HeaderGenerator() {}

  public static HttpHeaders headers(String targetName) {
    if (ExtensionConstant.HTML.equals(StringUtils.substringAfterLast(targetName, FILE_SEPARATOR))) {
      return htmlHeader(targetName);
    }
    return pdfHeader(targetName);
  }

  public static HttpHeaders pdfHeader(String fileName) {
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fileName);
    headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_PDF_VALUE));
    return headers;
  }

  public static HttpHeaders htmlHeader(String fileName) {
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fileName);
    headers.setContentType(MediaType.valueOf(MediaType.TEXT_HTML_VALUE));
    return headers;
  }
}
