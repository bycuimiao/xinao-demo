/*
 * Created by houyefeng on 2016-10-19.
 */

package com.xinao.common.util;

import com.xinao.common.Limit;
import com.xinao.common.OrderBy;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于业务层向数据访问层传递参数.
 *
 * @author houyefeng
 * @version 0.0.1
 * @since 0.0.1 2016-10-19
 */
public class Parameters implements Serializable {
  private static final long serialVersionUID = 7575610382439848078L;
  private List<String> paramKeys = new ArrayList<>(10); //支持的参数名
  private List<Parameter> params = new ArrayList<>(10);
  private List<OrderBy> orderBies = new ArrayList<>(5);
  private List<String> groupBies = new ArrayList<>(5);
  private Limit limit;


  /**
   * 增加排序条件.
   *
   * @param order 排序条件
   * @return {@link Parameters}
   */
  public Parameters addOrderBy(OrderBy order) {
    orderBies.add(order);
    return this;
  }

  public List<OrderBy> getOrderBies() {
    return orderBies;
  }

  /**
   * 增加分组条件.
   *
   * @param column 分组字段
   * @return {@link Parameters}
   */
  public Parameters addGroupBy(String column) {
    groupBies.add(column);
    return this;
  }

  public List<String> getGroupBies() {
    return groupBies;
  }

  public Limit getLimit() {
    return limit;
  }

  public void setLimit(Limit limit) {
    this.limit = limit;
  }

  /**
   * 将参数列表转换为Map，map的key形式为：{@code param.getName+ "_" + param.getOperator().getName()}，
   * param.getOperator().getName()==null时不进行拼接.
   *
   * @return 参数
   */
  public Map<String, Object> getParameterMap() {
    Map<String, Object> paramMap = new HashMap<>(params.size());
    for (Parameter param : params) {
      String paramKey = param.getName();
      if (StringUtils.isNotBlank(param.getOperator().getName())) {
        paramKey += ("_" + param.getOperator().getName());
      }
      paramMap.put(paramKey, param.getValue());
    }

    //order by
    if (!orderBies.isEmpty()) {
      paramMap.put("orderBies", orderBies);
    }
    //group by
    if (!groupBies.isEmpty()) {
      paramMap.put("groupBies", groupBies);
    }
    //limit
    if (null != limit) {
      paramMap.put("limit", limit);
    }
    return paramMap;
  }

  /**
   * 添加支持的参数名称.
   *
   * @param columns 支持的参数
   */
  public void addSupportParameters(List<String> columns) {
    paramKeys.addAll(columns);
  }

  /**
   * 添加支持的列.
   *
   * @param column 支持的列
   * @return {@code Parameters}
   */
  public Parameters addSupportParameter(String column) {
    paramKeys.add(column);
    return this;
  }

  /**
   * 添加支持的列.
   *
   * @param column 支持的列
   * @return {@code Parameters}
   */
  public Parameters add(String column) {
    return addSupportParameter(column);
  }

  /**
   * 将列名和值放入列表中，如果放入不支持的列将抛出{@link IllegalArgumentException}.
   *
   * @param column 字段名
   * @param val    字段值
   * @param operator 操作符
   * @throws IllegalArgumentException 放入不支持的列名时抛出
   * @see Parameter
   * @see Parameter.Operator
   */
  public void put(String column, Object val, Parameter.Operator operator) {
    if (paramKeys.contains(column)) {
      Parameter param = new Parameter();
      param.setOperator(operator);
      param.setName(column);
      param.setValue(val);
      params.add(param);
    } else {
      throw new IllegalArgumentException("not support parameter '" + column + "'");
    }
  }

  /**
   * 等于.
   *
   * @param column 字段名
   * @param val    字段值
   * @return {@link Parameters}
   * @see #put(String, Object, Parameter.Operator)
   */
  public Parameters eq(String column, Object val) {
    put(column, val, Parameter.Operator.EQ);
    return this;
  }

  /**
   * 大于.
   *
   * @param column 字段名
   * @param val    字段值
   * @return {@link Parameters}
   * @see #put(String, Object, Parameter.Operator)
   */
  public Parameters gt(String column, Object val) {
    put(column, val, Parameter.Operator.GT);
    return this;
  }

  /**
   * 大于等于.
   *
   * @param column 字段名
   * @param val    字段值
   * @return {@link Parameters}
   * @see #put(String, Object, Parameter.Operator)
   */
  public Parameters ge(String column, Object val) {
    put(column, val, Parameter.Operator.GE);
    return this;
  }

  /**
   * 小于.
   *
   * @param column 字段名
   * @param val    字段值
   * @return {@link Parameters}
   * @see #put(String, Object, Parameter.Operator)
   */
  public Parameters lt(String column, Object val) {
    put(column, val, Parameter.Operator.LT);
    return this;
  }

  /**
   * 小于等于.
   *
   * @param column 参数名
   * @param val    参数值
   * @return {@link Parameters}
   * @see #put(String, Object, Parameter.Operator)
   */
  public Parameters le(String column, Object val) {
    put(column, val, Parameter.Operator.LE);
    return this;
  }

  /**
   * 包含.
   *
   * @param column 参数名
   * @param val    参数值
   * @return {@link Parameters}
   * @see #put(String, Object, Parameter.Operator)
   */
  public Parameters in(String column, Object val) {
    put(column, val, Parameter.Operator.IN);
    return this;
  }
}
