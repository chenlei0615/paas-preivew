package com.meicloud.apaas.preview.core.common;

import lombok.Data;

/**
 * @author chenlei140
 * @className RestResult
 * @description 统一返回
 * @date 2021/12/23 9:36
 */
@Data
public class RestResult<T> {
  private boolean result;
  private int code;
  private String message;
  private T data;

  public RestResult(boolean result, ErrorCodeEnum exception, T o) {
    this.result = result;
    code = exception.getCode();
    message = exception.getMsg();
    data = o;
  }
}
