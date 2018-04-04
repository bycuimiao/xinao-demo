/*
 * Created by houyefeng on 2016-10-19.
 */

package com.xinao.common.interceptor;


import com.xinao.common.Limit;

/**
 * SQL方言处理工具，在MyBaits拦截器处理查询请求时根据方言对SQL进行处理.
 *
 * @author houyefeng
 * @version 0.0.1
 * @see PaginationInterceptor
 * @since 0.0.1 2016-10-19
 */
public abstract class Dialect {

  public enum Type {
    MYSQL,
    ORACLE
  }

  /**
   * 将原始SQL转换为有分页功能的SQL.
   *
   * @param sql   原SQL
   * @param limit 分页参数
   * @return 有分页功能的SQL
   */
  public abstract String getPageSql(String sql, Limit limit);

  /**
   * 生成用于count的SQL.
   *
   * @param sql 原SQL
   * @return 执行count的SQL
   */
  public abstract String getCountSql(String sql);

  /**
   * 判断sql是不是一个有分页功能的sql.
   *
   * @param sql 查询语句
   * @return 是分页SQL返回true，否则返回false
   */
  public abstract boolean isPageSql(String sql);
}
