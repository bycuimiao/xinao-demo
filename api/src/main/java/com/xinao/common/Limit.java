/*
 * Created by houyefeng on 2016-10-19.
 */

package com.xinao.common;

import java.io.Serializable;

/**
 * 用于API向Service传分页参数，同时记录总数会在{@code Limit#total}中返回.
 *
 * @author houyefeng
 * @version 0.0.1
 * @since 0.0.1 2016-10-19
 */
public class Limit implements Serializable {
  private static final long serialVersionUID = 522608975533031367L;
  private int length = 10;
  private int offset = 0;
  //总记录数，mybaits分页插件使用
  private int total;

  public Limit() {
  }

  public Limit(int offset, int length) {
    this.length = length;
    this.offset = offset;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public int getOffset() {
    return offset;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }
}
