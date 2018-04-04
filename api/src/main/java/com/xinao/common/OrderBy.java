/*
 * Created by houyefeng on 2016-10-19.
 */

package com.xinao.common;

import java.io.Serializable;

/**
 * 用于在sql中拼排序条件.
 *
 * @author houyefeng
 * @version 0.0.1
 * @since 0.0.1 2016-10-19
 */
public class OrderBy implements Serializable {
  private static final long serialVersionUID = 6526524148528103020L;
  private String column;
  private String order;

  public OrderBy(String column, Order order) {
    this.column = column;
    this.order = order.name();
  }

  public String getColumn() {
    return column;
  }

  public String getOrder() {
    return order;
  }

  public enum Order {
    ASC, DESC;
  }
}
