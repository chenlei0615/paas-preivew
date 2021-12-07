package com.meicloud.ship.preview.core.processor;

import com.meicloud.ship.preview.core.constants.DocumentFormatEnum;
import lombok.extern.slf4j.Slf4j;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.document.DocumentFormat;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author chenlei140
 * @className OfficeConvert
 * @description TODO
 * @date 2021/12/1 15:38
 */
@Slf4j
@Component
public class OfficeConvert {
//    @Resource
//    private DocumentConverter documentConverter;

    /**
     * @param inputStream  源文件输入流
     * @param outputStream pdf目标输出流
     */
    public void convert(InputStream inputStream, OutputStream outputStream, String suffix) throws Exception {
        final DocumentFormatEnum documentFormatEnum = DocumentFormatEnum.valueOf(suffix.toUpperCase());
        try (final InputStream is = documentFormatEnum.getInputStream(inputStream)) {
            final DocumentFormat format = documentFormatEnum.getFormFormat();
            log.info(">>> 待转换的文档类型：{}", format);
            final DocumentFormat targetFormat = documentFormatEnum.getTargetFormat();
            log.info(">>> 转换的目标文档类型：{}", targetFormat);
//            documentConverter.convert(is).as(format).to(outputStream).as(targetFormat).execute();
        }
        log.info(">>> 文件转换结束");
    }
}
