package com.meicloud.ship.preview.service.impl;

import com.meicloud.ship.preview.core.common.ErrorCodeEnum;
import com.meicloud.ship.preview.core.constants.DocumentFormatEnum;
import com.meicloud.ship.preview.core.exception.FilePreviewException;
import com.meicloud.ship.preview.core.processor.OfficeConvert;
import com.meicloud.ship.preview.core.utils.FileUtil;
import com.meicloud.ship.preview.service.PreviewService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Objects;

@Slf4j
@Service
public class PreviewImpl implements PreviewService {
    @Autowired
    private OfficeConvert officeConvert;

    @Autowired
    private HttpServletResponse response;

    @Override
    public void convertFile2pdfByUrl(String fileUrl) throws Exception{
        if (StringUtils.isBlank(fileUrl)) {
            throw new FilePreviewException(ErrorCodeEnum.PARAM_ERROR.getCode(), "请传入文件url地址");
        }

        InputStream in = FileUtil.getInputStreamByUrl(fileUrl);
        if (in == null) {
            throw new FilePreviewException(ErrorCodeEnum.ERROR.getCode(), "解析url地址失败");
        }

        String fileExtName = FileUtil.getExtension(in);
        if (StringUtils.isBlank(fileExtName)) {
            throw new FilePreviewException(ErrorCodeEnum.ERROR.getCode(), "解析文件扩展名地址失败");
        }
        try (final ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try {
                final DocumentFormatEnum documentFormatEnum = DocumentFormatEnum.valueOf(fileExtName.toUpperCase());
                if (!Objects.isNull(documentFormatEnum)) {
                    officeConvert.convert(in, bos, fileExtName);
                }
            } catch (IllegalArgumentException e) {
                bos.reset();
                int ch;
                while ((ch = in.read())!= -1) {
                    bos.write(ch);
                }
            }
            final byte[] bytes = bos.toByteArray();
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Length", String.valueOf(bytes.length));
            ServletOutputStream out = response.getOutputStream();
            out.write(bytes);
            out.flush();
        }
    }
}
