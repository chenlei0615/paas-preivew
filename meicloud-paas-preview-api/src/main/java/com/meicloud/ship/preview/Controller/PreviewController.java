package com.meicloud.ship.preview.Controller;

import com.meicloud.ship.preview.core.constants.DocumentFormatEnum;
import com.meicloud.ship.preview.core.processor.PreviewProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author chenlei140
 * @className PreviewController
 * @description TODO
 * @date 2021/11/30 11:23
 */
@Slf4j
@RestController
@RequestMapping("v1")
public class PreviewController {

    @Autowired
    private PreviewProcessor previewProcessor;

    @PostMapping("/upload")
    public void upload(MultipartFile file, HttpServletResponse response) throws Exception {
        log.info(">>> file.getName()：【{}】，file.getOriginalFilename()：【{}】，file.getSize()：【{}】", file.getName(), file.getOriginalFilename(), file.getSize());
        final String fileSuffix = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");

        try (final ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try {
                final DocumentFormatEnum documentFormatEnum = DocumentFormatEnum.valueOf(fileSuffix.toUpperCase());
                if (!Objects.isNull(documentFormatEnum)) {
                    previewProcessor.convert(file.getInputStream(), bos, fileSuffix);
                }
            } catch (IllegalArgumentException e) {
                bos.reset();
                bos.write(file.getBytes(), 0, file.getBytes().length);
            }
            bos.close();
            final byte[] bytes = bos.toByteArray();
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Length", String.valueOf(bytes.length));
            ServletOutputStream out = response.getOutputStream();
            out.write(bytes);
            out.flush();
        }
    }
}
