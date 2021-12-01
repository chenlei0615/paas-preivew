package com.meicloud.ship.preview.core.processor;

import com.meicloud.ship.preview.core.config.OfficeManagerInstance;
import com.meicloud.ship.preview.core.constants.DocumentFormatEnum;
import lombok.extern.slf4j.Slf4j;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.document.DocumentFormat;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.local.LocalConverter;
import org.jodconverter.local.office.LocalOfficeManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author chenlei140
 * @className PreviewProcessor
 * @description 文档转换器
 * @date 2021/11/24 9:06
 */
@Slf4j
public class PreviewProcessor {

    private PreviewProcessor(){}

    /**
     * @param inputStream  源文件输入流
     * @param outputStream pdf目标输出流
     */
    public static void convert(InputStream inputStream, OutputStream outputStream, String suffix) {
        LocalOfficeManager localOfficeManager = OfficeManagerInstance.start();
        DocumentConverter converter = LocalConverter.builder().officeManager(localOfficeManager).build();
        final DocumentFormatEnum documentFormatEnum = DocumentFormatEnum.valueOf(suffix.toUpperCase());
        try (final InputStream is = documentFormatEnum.getInputStream(inputStream)) {
            final DocumentFormat format = documentFormatEnum.getFormFormat();
            log.info(">>> 待转换的文档类型：{}", format);
            final DocumentFormat targetFormat = documentFormatEnum.getTargetFormat();
            log.info(">>> 转换的目标文档类型：{}", targetFormat);
            converter.convert(is).as(format).to(outputStream).as(targetFormat).execute();
        } catch (IOException | OfficeException e) {
            log.error(">>> 转换的目标文档异常：{}", e.getStackTrace());
        }
        log.info(">>> 文件转换结束");
    }

}
