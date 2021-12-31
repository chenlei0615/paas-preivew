package com.meicloud.ship.preview.core.common;

import org.apache.http.HttpStatus;

/**
 * @author chenlei140
 * @className ErrorCodeEnum
 * @description 错误枚举
 * @date 2021/10/13 17:11
 */
public enum ErrorCodeEnum implements HttpStatus {
    /**
     * 错误枚举： 错误码->提示信息
     */
    SUCCESS(200, "成功"),
    ERROR(500, "系统错误"),
    BIND_ERROR(400, "请求失败"),
    NO_TOKEN(401, "无token，请重新登录"),
    ERROR_TOKEN(402, "token异常，请重新登陆"),
    FORBIDDEN(403, "用户验证失败"),
    IP_NOT_PERMIT(501, "IP address is not permit"),
    PARAM_ERROR(502, "参数错误"),
    REPEAT_SUBMIT(601, "请勿重复提交!"),
    PARAM_EXCEPTION(10001, "参数异常"),
    FILE_NOT_EXIST(10002, "文件不存在或文件为空"),
    FILE_URL_NOT_EXIST(10003, "请传入文件url地址"),
    PARSE_FILE_URL_FAILED(10004, "解析url地址失败"),
    FILE_OVERSIZE(10005, "上传文件过大"),
    ILLEGAL_ARGUMENT(10006, "当前参数不合法");


    private Integer code;

    private String msg;


    ErrorCodeEnum(Integer status, String msg) {
        this.code = status;
        this.msg = msg;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public static ErrorCodeEnum getByCode(Integer status) {
        ErrorCodeEnum tab = null;
        ErrorCodeEnum[] values = ErrorCodeEnum.values();
        for (ErrorCodeEnum value : values) {
            if (status.equals(value.code)) {
                tab = value;
            }
        }
        return tab;
    }
}
