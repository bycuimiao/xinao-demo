package com.xinao.common;

/**
 * @auther cuimiao
 * @date 2018/2/25/025  17:32
 * @Description: ${todo}
 */
public enum State {
    SUCCESS("success"),
    FAILED("failed");

    private String code;

    State(String code) {
        this.code = code;
    }
}
