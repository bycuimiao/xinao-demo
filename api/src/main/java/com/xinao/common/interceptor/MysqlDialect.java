/*
 * Created by houyefeng on 2016-10-19.
 */

package com.xinao.common.interceptor;

import com.xinao.common.Limit;

/**
 * 基于MySql的{@code Dialect}.
 *
 * @author houyefeng
 * @version 0.0.1
 * @see Dialect
 * @since 0.0.1 2016-10-19
 */
public class MysqlDialect extends Dialect {

  /**
   * 将原始SQL转换为有分页功能的SQL.
   *
   * @param sql   原SQL
   * @param limit 分页参数
   * @return 有分页功能的SQL
   */
  @Override
  public String getPageSql(String sql, Limit limit) {
    StringBuffer pagingSelect = new StringBuffer(sql.length() + 50);

    pagingSelect.append(sql);
    pagingSelect.append(" limit ").append(limit.getOffset()).append(",").append(limit.getLength());

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
    StringBuffer countSql = new StringBuffer(sql.length() + 20);
    String tempSql = sql.toLowerCase();
    int orderIndex = tempSql.lastIndexOf("order by");

    if (haveGroup(tempSql)) {
      countSql.insert(0, "SELECT count(*) FROM (");
      if (orderIndex > -1) {
        countSql.append(sql.substring(0, orderIndex).trim());
      } else {
        countSql.append(sql.trim());
      }
      countSql.append(") t");
    } else {
      int fromIndex = tempSql.indexOf(" from");
      //去掉原sql中第一个from前和最后一个order by后的部分
      if (orderIndex > -1) {
        countSql.append(sql.substring(fromIndex, orderIndex).trim());
      } else {
        countSql.append(sql.substring(fromIndex).trim());
      }
      countSql.insert(0, "SELECT count(*) ");
    }
    return countSql.toString();
  }

  private boolean haveGroup(String tempSql) {
    int whereIndex = tempSql.lastIndexOf("where");
    if (whereIndex == -1) {
      return false;
    }

    int groupIndex = tempSql.indexOf("group by", whereIndex); //查找最后一个where后面有没有group by
    if (groupIndex > -1) { //如果有group by，查找group by后是否有')'，以此来判断group by是否在嵌套sql中
      if (tempSql.indexOf(")", groupIndex) == -1) {
        return true;
      }
    }

    return false;
  }

  /**
   * 判断sql是不是一个有分页功能的sql.
   *
   * @param sql 查询语句
   * @return 是分页SQL返回true，否则返回false
   */
  public boolean isPageSql(String sql) {
    return sql.replaceAll("\\n", "").trim().matches("((.*)limit[ ]*[0-9]*,[ ]*[0-9]*)$");
  }
}
