package com.meicloud.ship.preview.core.common;

/**
 * @author chenlei140
 * @className ResultCode
 * @description 错误枚举
 * @date 2021/10/13 17:11
 */
public enum ErrorCodeEnum {
    /**
     * 错误枚举： 错误码->提示信息
     */
    SUCCESS(Result.SUCCESS, "成功")
    ,ERROR(Result.ERROR, "系统错误")
    ,BIND_ERROR(400, "请求失败")
    ,NO_TOKEN(401, "无token，请重新登录")
    ,ERROR_TOKEN(402, "token异常，请重新登陆")
    ,FORBIDDEN(403, "用户验证失败")
    ,IP_NOT_PERMIT(501, "IP address is not permit")
    ,PARAM_ERROR(502, "参数错误")
    ,REPEAT_SUBMIT(601, "请勿重复提交!");

    private Integer code;

    private String msg;


    /**
     * 拿到Result对象
     * @return
     */
    public Result get(){
        return new Result().setCode(this.code).setMsg(this.msg);
    }

    ErrorCodeEnum(Integer status, String msg){
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


    public static ErrorCodeEnum getByCode(Integer status){
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
