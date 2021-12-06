package com.meicloud.ship.preview.core.utils;

import com.sun.star.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.metadata.HttpHeaders;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaMetadataKeys;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.xml.sax.helpers.DefaultHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Slf4j
public class FileUtil {
    /**
     * 根据地址获得数据的输入流
     *
     * @param strUrl 网络连接地址
     * @return url的输入流
     */
    public static InputStream getInputStreamByUrl(String strUrl) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(20 * 1000);
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy(conn.getInputStream(), output);
            return new ByteArrayInputStream(output.toByteArray());
        } catch ( Exception e) {
            log.error("解析url地址报错: {}", e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    /**
     * 获取文件扩展名
     *
     * @param inputStream 输入流
     * @return 文件类型
     */
    public static String getExtension(InputStream inputStream) throws Exception {
        AutoDetectParser parser = new AutoDetectParser();
        parser.setParsers(new HashMap<>());

        Tika tika = new Tika();
        String contentType = tika.detect(inputStream);
        Metadata metadata = new Metadata();
        metadata.add(TikaMetadataKeys.RESOURCE_NAME_KEY, contentType);
        parser.parse(inputStream, new DefaultHandler(), metadata, new ParseContext());
        inputStream.close();
        String mime = metadata.get(HttpHeaders.CONTENT_TYPE);
        String extType = checkType(mime);
        return extType;
    }

    private static String checkType(String mime) {
        switch (mime) {
            case "application/msword": {
                return "doc";
            }
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document": {
                return "docx";
            }
            case "application/vnd.ms-excel": {
                return "xls";
            }
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet": {
                return "xlsx";
            }

            case "application/vnd.ms-powerpoint": {
                return "ppt";
            }

            case "application/vnd.openxmlformats-officedocument.presentationml.presentation": {
                return "pptx";
            }

            case "application/x-rar-compressed": {
                return "rar";
            }

            case "application/zip": {
                return "zip";
            }

            case "application/octet-stream": {
                return "txt";
            }

            case "text/html": {
                return "html";
            }

            case "image/png": {
                return "png";
            }

            case "image/jpeg": {
                return "jpeg";
            }
            default: {
                return "";
            }
        }
    }
}
