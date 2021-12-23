package com.meicloud.ship.preview.config;

import com.meicloud.ship.preview.core.common.RestResult;
import com.meicloud.ship.preview.core.exception.FilePreviewException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author chenlei140
 * @className RestExceptionHandler
 * @description RestExceptionHandler
 * @date 2021/12/23 9:21
 */
@RestControllerAdvice(annotations = RestController.class)
public class RestExceptionHandler {

    /**
     * 只要抛出该类型异常,则由此方法处理
     * 并由此方法响应出异常状态码及消息
     *
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = FilePreviewException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestResult handleException(Exception e) {
        return new RestResult(false, ((FilePreviewException) e).getException(), null);
    }

}
