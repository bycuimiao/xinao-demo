/*
 * Created by houyefeng on 2016-10-19.
 */

package com.xinao.common.interceptor;

import com.xinao.common.Limit;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 基于MyBaits插件SQL转换拦截器，为需要统计总数的查询操作增加count语句，并将结果通过{@link Limit}返回.
 *
 * <p>如果执行的法以find开头，并且mapper中的SQL语句中没有｛@code limit}关键字，将自动增加一个由原SQL转换来的{@code select count(*) from table where }语句，
 * 执行结果写入{@link Limit#setTotal(int)}中。
 * </p>
 *
 * @author houyefeng
 * @version 0.0.1
 * @see Limit
 * @see Dialect
 * @see MysqlDialect
 * @since 0.0.1 2016-09-23
 */
/*@Intercepts(value = {@Signature(type = StatementHandler.class, method = "prepare",
    args = {Connection.class})})*/ //3.40之前的写法
@Intercepts(value = {@Signature(type = StatementHandler.class, method = "prepare",
    args = {Connection.class,Integer.class})})//3.40之后的写法
public class PaginationInterceptor implements Interceptor {

  //日志对象
  private static Logger log = LoggerFactory.getLogger(PaginationInterceptor.class);

  private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
  private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
    ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
    MetaObject metaStatementHandler = MetaObject.forObject(statementHandler,
        DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, reflectorFactory);
    /* 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环
    可以分离出最原始的的目标类)
     */
    while (metaStatementHandler.hasGetter("h")) {
      Object object = metaStatementHandler.getValue("h");
      ReflectorFactory reflectorFactorys = metaStatementHandler.getReflectorFactory();
      metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY,
          DEFAULT_OBJECT_WRAPPER_FACTORY, reflectorFactory);
    }
    // 分离最后一个代理对象的目标类
    while (metaStatementHandler.hasGetter("target")) {
      Object object = metaStatementHandler.getValue("target");
      ReflectorFactory reflectorFactorys = metaStatementHandler.getReflectorFactory();
      metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY,
          DEFAULT_OBJECT_WRAPPER_FACTORY, reflectorFactory);
    }
    Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");

    //得到dialect，默认为 Dialect.Type.MYSQL;
    Dialect dialect = getDialect(configuration);

    MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
    BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");

    //判断本次查询是否需要分页
    if (needPaging(dialect, configuration, mappedStatement, boundSql)) {
      //需要分页时先进行count，如果count大于0才将sql重写为分页sql
      Limit limit = (Limit) ((Map) boundSql.getParameterObject()).get("limit");
      executeCount(dialect, invocation, mappedStatement, boundSql, limit);
      if (limit.getTotal() > 0) {
        String sql = boundSql.getSql();
        //重定sql
        String pageSql = dialect.getPageSql(sql, limit);
        metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);
        //实现了数据库的分页后，就不需要mybatis的内存分页了，所以重置下面的两个参数
        metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
        metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
      }
    }
    return invocation.proceed();
  }

  /**
   * 支持count语句，将结果写到{@link Limit}中.
   *
   * @param dialect         {@link Dialect}
   * @param invocation      {@code Invocation}
   * @param mappedStatement {@code MappedStatement}
   * @param boundSql        MyBaits配置文件中配置的sql
   * @param limit           {@code Limit}
   * @see MysqlDialect
   * @see OracleDialect
   */
  private void executeCount(Dialect dialect, Invocation invocation, MappedStatement mappedStatement, BoundSql boundSql, Limit limit) {
    String countSql = dialect.getCountSql(boundSql.getSql());
    log.debug("count sql:{}", countSql);

    PreparedStatement countStmt = null;
    ResultSet rs = null;

    try {
      Connection connection = (Connection) invocation.getArgs()[0];
      countStmt = connection.prepareStatement(countSql);
      BoundSql countbs = new BoundSql(mappedStatement.getConfiguration(), countSql,
          boundSql.getParameterMappings(), boundSql.getParameterObject());

      setAdditionalParameters(boundSql, countbs);

      setParameters(countStmt, mappedStatement, countbs, boundSql.getParameterObject());

      rs = countStmt.executeQuery();
      int total = 0;
      if (rs.next()) {
        total = rs.getInt(1);
      }
      limit.setTotal(total);
    } catch (SQLException e) {
      log.warn("pagination count error", e);
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
      } catch (SQLException e) {
        log.warn("pagination count error", e);
      }
      try {
        if (countStmt != null) {
          countStmt.close();
        }
      } catch (SQLException e) {
        log.warn("pagination count error", e);
      }
    }
  }

  /**
   * 通过反射复制{@code sourceBoundSql}中的{@code BoundSql#additionalParameters}值到{@code targetBoundSql}的{@code BoundSql#additionalParameters}.
   *
   * @param sourceBoundSql 源{@code BoundSql}
   * @param targetBoundSql 目标{@code BoundSql}
   * @see BoundSql#additionalParameters
   */
  private void setAdditionalParameters(BoundSql sourceBoundSql, BoundSql targetBoundSql) throws SQLException {
    Class<? extends Object> sourceClass = sourceBoundSql.getClass();

    Field addParamsField = null;
    try {
      addParamsField = sourceClass.getDeclaredField("additionalParameters");
    } catch (NoSuchFieldException e) {
      throw new SQLException("net exists field 'additionalParameters'", e);
    }

    addParamsField.setAccessible(true);

    Map<String, Object> additionalParams = null;
    try {
      additionalParams = (Map<String, Object>) addParamsField.get(sourceBoundSql);
    } catch (IllegalAccessException e) {
      throw new SQLException("cannot access field 'additionalParameters'", e);
    }

    if (additionalParams != null && !additionalParams.isEmpty()) {
      Set<String> keys = additionalParams.keySet();
      for (String key : keys) {
        if (targetBoundSql.hasAdditionalParameter(key)) {
          log.warn("{} already exists, ignored");
        } else {
          targetBoundSql.setAdditionalParameter(key, additionalParams.get(key));
        }
      }
    }

  }

  /**
   * 设置{@link PreparedStatement}的值.
   *
   * @param ps              {@code PreparedStatement}
   * @param mappedStatement {@code MappedStatement}
   * @param boundSql        MyBaits配置文件中配置的sql语句
   * @param parameterObject 参数值
   * @throws SQLException 参考{@link ParameterHandler#setParameters(PreparedStatement)}
   */
  private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject) throws SQLException {
    ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
    parameterHandler.setParameters(ps);
  }

  /**
   * 确定本次查询是否需要分页.
   *
   * <p>分页条件：
   * <ul>
   * <li>mapper中sql id以从{@link Configuration}中获取的pagingSqlId的值开头，默认值为"find"</li>
   * <li>参数类型为{@link Map}</li>
   * <li>参数中有以"limit"为key，值为{@link Limit}对象</li>
   * </ul>
   * </p>
   *
   * @param dialect         {@code Dialect}
   * @param configuration   MyBaits配置文件
   * @param mappedStatement {@code MappedStatement}
   * @return 需要分分页返回true，否则返回false
   */
  private boolean needPaging(Dialect dialect, Configuration configuration, MappedStatement mappedStatement, BoundSql boundSql) {
    String pagingSqlid = "find";
    Properties properties = configuration.getVariables();
    if (null != properties) {
      pagingSqlid = properties.getProperty("pagingSqlId", "find");
    }
    boolean need;

    need = getSqlIdNoNs(mappedStatement).startsWith(pagingSqlid);

    log.debug("sqlId is {}, paging sqlId need start with {}, match:{}", new Object[]{mappedStatement.getId(), pagingSqlid, need});

    //如果是以pagingSqlId指定的值开头，则继续判断参数口有没有limit参数
    if (need) {
      Object parameterObject = boundSql.getParameterObject();
      need = parameterObject instanceof Map;
      if (need) {
        Object limit = ((Map) parameterObject).get("limit");
        need = (null != limit && limit instanceof Limit);
      }
    }

    //如果有limit参数，看sql中是否已有分页功能
    if (need) {
      need = !dialect.isPageSql(boundSql.getSql());
    }
    return need;
  }

  /**
   * 不包含namespace的sql id.
   *
   * @param ms {@link MappedStatement}
   * @return sql id
   */
  private String getSqlIdNoNs(MappedStatement ms) {
    String sqlId = ms.getId();
    String[] arr = sqlId.split("\\.");
    return arr[arr.length - 1];
  }

  /**
   * 得到{@link Dialect}，默认为 {@link Dialect.Type#MYSQL}.
   *
   * @param configuration MyBaits配置文件
   * @return {@code Dialect}
   */
  private Dialect getDialect(Configuration configuration) {
    Dialect.Type dialectType = Dialect.Type.MYSQL;
    String dt = "";
    try {
      Properties properties = configuration.getVariables();
      if (null != properties) {
        dt = properties.getProperty("dialect", Dialect.Type.MYSQL.toString());
      }
      dialectType = Dialect.Type.valueOf(dt.toUpperCase());
    } catch (Exception e) {
      log.warn("dialect '{}' is not supported,use default 'MYSQL'", dt);
    }
    Dialect dialect;
    switch (dialectType) {
      case ORACLE:
        dialect = new OracleDialect();
        break;
      default:
        dialect = new MysqlDialect();
    }
    return dialect;
  }

  /* (non-Javadoc)
   * @see org.apache.ibatis.plugin.Interceptor#plugin(java.lang.Object)
   */
  @Override
  public Object plugin(Object target) {
    //目标是StatementHandler时才进行拦截，以减少目标类被代理的次数
    /*if (target instanceof StatementHandler) {
      return Plugin.wrap(target, this);
    }
    return target;*/
    return Plugin.wrap(target, this);
  }

  /* (non-Javadoc)
   * @see org.apache.ibatis.plugin.Interceptor#setProperties(java.util.Properties)
   */
  @Override
  public void setProperties(Properties prop) {

  }

}
