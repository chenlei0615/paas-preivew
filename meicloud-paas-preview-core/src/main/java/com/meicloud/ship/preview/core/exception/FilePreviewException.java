package com.meicloud.ship.preview.core.exception;

import com.meicloud.ship.preview.core.common.ErrorCodeEnum;

public class FilePreviewException extends RuntimeException {

    private ErrorCodeEnum errorCodeEnum;

    public FilePreviewException(ErrorCodeEnum errorCodeEnum) {
        this.errorCodeEnum = errorCodeEnum;
    }

    public ErrorCodeEnum getException() {
        return errorCodeEnum;
    }

    public void setException() {
        this.errorCodeEnum = errorCodeEnum;

    }
}
