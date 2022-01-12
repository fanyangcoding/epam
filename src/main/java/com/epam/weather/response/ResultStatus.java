package com.epam.weather.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ResultStatus {
    SUCCESS("0", "success"),
    FAIL("-1", "fail");

    /**
     * 返回码
     */
    private String code;

    /**
     * 返回结果描述
     */
    private String msg;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
