/*
 * Created by houyefeng on 2016-10-19.
 */

package com.xinao.common.interceptor;


import com.xinao.common.Limit;

/**
 * 基于Oracle的{@code Dialect}.
 *
 * @author houyefeng
 * @version 0.0.1
 * @see Dialect
 * @since 0.0.1 2016-10-19
 */
public class OracleDialect extends Dialect {

  /**
   * 将原始SQL转换为有分页功能的SQL.
   *
   * @param sql   原SQL
   * @param limit 分页参数
   * @return 有分页功能的SQL
   */
  @Override
  public String getPageSql(String sql, Limit limit) {
    sql = sql.trim();
    StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);

    pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");

    pagingSelect.append(sql);

    pagingSelect.append(" ) row_ ) where rownum_ > ")
        .append(limit.getOffset())
        .append(" and rownum_ <= ")
        .append(limit.getOffset() + limit.getLength());

    return pagingSelect.toString();
  }

  /**
   * 生成用于count的SQL.
   *
   * @param sql 原SQL
   * @return 执行count的SQL
   */
  @Override
  public String getCountSql(String sql) {
    return null;
  }

  /**
   * 判断sql是不是一个有分页功能的sql.
   *
   * @param sql 查询语句
   * @return 是分页SQL返回true，否则返回false
   */
  @Override
  public boolean isPageSql(String sql) {
    return false;
  }

}
