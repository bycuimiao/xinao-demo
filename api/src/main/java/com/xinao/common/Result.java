package com.xinao.common;

import java.io.Serializable;

/**
 * @auther cuimiao
 * @date 2018/2/25/025  17:26
 * @Description: ${todo}
 */
public class Result<D,C> implements Serializable {
    private static final long serialVersionUID = -6748000674120937096L;
    private C code;
    private D data;
    private String message;

    public Result() {
    }

    public C getCode() {
        return code;
    }

    public void setCode(C code) {
        this.code = code;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
