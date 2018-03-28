/*
 * Created by houyefeng on 2016-10-19.
 */

package com.xinao.common.util;

import java.io.Serializable;

/**
 * 用于业务层向数据访问层传递参数的参数类.
 *
 * @author houyefeng
 * @version 0.0.1
 * @since 0.0.1 2016-10-19
 */
public class Parameter implements Serializable {
  private static final long serialVersionUID = 4364633703379022510L;
  private String name;
  private Object value;
  private Operator operator;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public Operator getOperator() {
    return operator;
  }

  public void setOperator(Operator operator) {
    this.operator = operator;
  }

  public enum Operator {
    EQ("eq", "等于"),
    NE("ne", "不等于"),
    GT("gt", "大于"),
    GE("ge", "大于等于"),
    LT("lt", "小于"),
    LE("le", "小于等于"),
    IN("in", ""), NONE("", "无");

    private String name;
    private String desc;

    public String getName() {
      return name;
    }

    Operator(String name, String desc) {
      this.name = name;
      this.desc = desc;
    }

    @Override
    public String toString() {
      return "Operator{"
          + "name='" + name + '\''
          + ", desc='" + desc + '\''
          + '}';
    }
  }
}
