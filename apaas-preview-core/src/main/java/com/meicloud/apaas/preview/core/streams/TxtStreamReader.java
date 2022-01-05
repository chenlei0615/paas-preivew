package com.meicloud.apaas.preview.core.streams;

import java.io.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

/**
 * @author chenlei140
 * @className ExcelAssembler
 * @description txt配置装配
 * @date 2021/12/23 10:11
 */
@Scope(value = "prototype")
public class TxtStreamReader {

    private static final Logger logger = LoggerFactory.getLogger(TxtStreamReader.class);

    private TxtStreamReader() {}

    public static InputStream getInputStream(InputStream inputStream) {
        // 因为会出现中文乱码问题，所以先通过字符流进行编码转换，再转换成字节流
        try (BufferedReader bis = new BufferedReader(new InputStreamReader(inputStream, "GBK"))) {
            StringBuilder buf = new StringBuilder();
            String temp;
            while ((temp = bis.readLine()) != null) {
                buf.append(temp).append(System.getProperty("line.separator"));
            }
            return new ByteArrayInputStream(buf.toString().getBytes());
        } catch (UnsupportedEncodingException e) {
            logger.error(" 解码异常 : {} ", e.getStackTrace());
        } catch (IOException e) {
            logger.error(" IO异常 : {} ", e.getStackTrace());
        }
        return null;
    }
}
