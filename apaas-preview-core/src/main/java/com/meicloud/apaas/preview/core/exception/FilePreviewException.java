package com.meicloud.apaas.preview.core.exception;

import com.meicloud.apaas.preview.core.common.ErrorCodeEnum;

public class FilePreviewException extends RuntimeException {

  private static final long serialVersionUID = 4308464268891979835L;
  private ErrorCodeEnum errorCodeEnum;

  public FilePreviewException(ErrorCodeEnum errorCodeEnum) {
    this.errorCodeEnum = errorCodeEnum;
  }

  public ErrorCodeEnum getException() {
    return errorCodeEnum;
  }

  public void setException() {
    errorCodeEnum = errorCodeEnum;
  }
}
