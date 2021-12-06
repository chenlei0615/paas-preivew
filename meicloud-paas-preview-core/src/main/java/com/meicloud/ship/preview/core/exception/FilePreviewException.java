package com.meicloud.ship.preview.core.exception;

import com.meicloud.ship.preview.core.common.ErrorCodeEnum;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class FilePreviewException extends RuntimeException{
    private String message;
    private Integer code;

    public FilePreviewException() {
        super();
    }

    public FilePreviewException(String message) {
        code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.message = message;
    }

    public FilePreviewException(ErrorCodeEnum codeEnum) {
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMsg();
    }

    public FilePreviewException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
