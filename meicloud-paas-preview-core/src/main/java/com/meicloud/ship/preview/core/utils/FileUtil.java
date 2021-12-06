package com.meicloud.ship.preview.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class FileUtil {
    /**
     * 根据地址获得数据的输入流
     * @param strUrl 网络连接地址
     * @return url的输入流
     */
    public static InputStream getInputStreamByUrl(String strUrl){
        HttpURLConnection conn = null;
        try {
            URL url = new URL(strUrl);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(20 * 1000);
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy(conn.getInputStream(),output);
            return  new ByteArrayInputStream(output.toByteArray());
        } catch (Exception e) {
            log.error(e+"");
        }finally {
            try{
                if (conn != null) {
                    conn.disconnect();
                }
            }catch (Exception e){
                log.error(e+"");
            }
        }
        return null;
    }

    /**
     * 获取扩展名
     */
    public static String getExtension(InputStream inputStream) {
        try {
            Tika tika = new Tika();
            String contentType = tika.detect(inputStream);
            MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
            MimeType mime = allTypes.forName(contentType);
            return mime.getExtension().substring(1);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }
}
