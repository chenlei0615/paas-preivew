package com.meicloud.ship.preview.core.common;

import java.io.Serializable;

/**
 * @author chenlei140
 * @className Result
 * @description 返回结果
 * @date 2021/10/13 17:12
 */
public class Result implements Serializable {

    public static final int SUCCESS = 200;

    public static final int ERROR = 500;

    /**
     * 状态
     */
    public Integer code;

    /**
     * 数据
     */
    public Object data;

    /**
     * 消息
     */
    public String msg;

    public Result setData(Object data) {
        this.data = data;
        return this;
    }
    public Integer getCode() {
        return code;
    }
    public Result setCode(Integer code) {
        this.code = code;
        return this;
    }
    public String getMsg() {
        return msg;
    }
    public Result setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
