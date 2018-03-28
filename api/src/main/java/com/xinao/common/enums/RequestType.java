/*
 * Created by guanshang on 2016-11-03.
 */

package com.xinao.common.enums;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-03
 */
public enum RequestType implements Enum<String> {
  GET("GET", "GET"), POST("POST", "POST"), PUT("PUT", "PUT"), PATCH("PATCH", "PATCH"), DELETE("DELETE", "DELETE");

  private String value;
  private String desc;

  RequestType(String value, String desc) {
    this.value = value;
    this.desc = desc;
  }

  @Override
  public String getValue() {
    return value;
  }

  public String getDesc() {
    return desc;
  }

  /**
   * 判断{@code val}是不是合法的枚举值.
   *
   * @param val 枚举值
   * @return 如果{@code val}是一个合法的枚举值返回true，否则返回false
   */
  public static boolean contents(String val) {
    RequestType[] values = RequestType.values();
    for (RequestType requestType : values) {
      if (requestType.getValue().equals(val)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return new StringBuilder("RequestType：")//
        .append("value=").append(value)//
        .append("desc=").append(desc)//
        .toString();
  }
}