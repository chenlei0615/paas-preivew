package com.meicloud.ship.preview.core.streams;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;

import java.io.*;

/**
 * @author chenlei140
 * @className ExcelAssembler
 * @description txt配置装配
 * @date 2021/12/23 10:11
 */
@Slf4j
@Scope(value = "prototype")
public class TxtStreamReader {
    private TxtStreamReader() {

    }

    public static InputStream getExcelStream(InputStream inputStream) {
        //因为会出现中文乱码问题，所以先通过字符流进行编码转换，再转换成字节流
        try (final BufferedReader bis = new BufferedReader(new InputStreamReader(inputStream, "GBK"))) {
            StringBuilder buf = new StringBuilder();
            String temp;
            while ((temp = bis.readLine()) != null) {
                buf.append(temp).append(System.getProperty("line.separator"));
            }
            return new ByteArrayInputStream(buf.toString().getBytes());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
