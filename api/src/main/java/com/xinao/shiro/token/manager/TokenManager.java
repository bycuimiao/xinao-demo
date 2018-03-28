/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.shiro.token.manager;

import com.xinao.common.cache.util.SpringContextUtil;
import com.xinao.entity.User;
import com.xinao.shiro.common.ShiroConstant;
import com.xinao.shiro.enums.SessionStatus;
import com.xinao.shiro.session.impl.CustomSessionManager;
import com.xinao.shiro.token.SampleRealm;
import com.xinao.shiro.token.ShiroToken;
import com.xinao.shiro.util.LoggerUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public class TokenManager {
  //session-key
  public static final String SESSION_ENTERPRISE = "enterprise";//企业
  public static final String SESSION_ASSOCIATE_DEPARTMENTS = "associateDepartments";//当前部门和关联下级部门

  //用户登录管理
  public static final SampleRealm realm = SpringContextUtil.getBean("sampleRealm", SampleRealm.class);
  //部门管理
  //public static final DepartmentService departmentService = SpringContextUtil.getBean("departmentService", DepartmentService.class);
  //用户session管理
  public static final CustomSessionManager customSessionManager = SpringContextUtil.getBean("customSessionManager", CustomSessionManager.class);

  //private static String ENN_ENTERPRISE = ConfigurationFactory.getConfiguration("config.properties").getString("enterprise.enn");

  /**
   * 获取当前登录的用户User对象.
   *
   * @return user对象
   */
  public static User getToken() {
    User token = (User) SecurityUtils.getSubject().getPrincipal();
    return token;
  }

  public static boolean isPermitted(String uri, String reqMethod) {
    return SecurityUtils.getSubject().isPermitted(uri + ShiroConstant.SPLIT + reqMethod);
  }


  /**
   * 获取当前用户的Session.
   *
   * @return 用户session
   */
  public static Session getSession() {
    return SecurityUtils.getSubject().getSession();
  }

  /**
   * 获取当前用户name.
   *
   * @return 手机号
   */
  public static String getNickname() {
    return getToken().getName();
  }

  /**
   * 获取当前用户ID.
   *
   * @return 用户ID
   */
  public static Long getUserId() {
    return getToken() == null ? Long.valueOf(0L) : getToken().getId();
  }

  /**
   * 获取当前用户的企业ID.
   *
   * @return 企业ID
   */
  /*public static Long getEnterpriseId() {
    return getToken() == null ? Long.valueOf(1L) : getToken().getEnterpriseId();
  }*/

  /**
   * 后获取当前用户的企业ID.
   *
   * @return 企业ID
   */
  /*public static Long getDepartmentId() {
    return getToken() == null ? Long.valueOf(1L) : getToken().getDepartmentId();
  }*/

  /**
   * 把值放入到当前登录用户的Session里.
   *
   * @param key   key
   * @param value value
   */
  public static void setVal2Session(Object key, Object value) {
    getSession().setAttribute(key, value);
  }

  /**
   * 从当前登录用户的Session里取值.
   *
   * @param key key
   * @return 获取value
   */
  public static Object getVal2Session(Object key) {
    return getSession().getAttribute(key);
  }

  /**
   * 获取的登录人的当前部门和级联下级部门.
   *
   * @return 关联部门信息
   */
  /*public static List<Long> getAssociateDepartments() {
    List<Long> associateDepartments = (List<Long>) getSession().getAttribute(SESSION_ASSOCIATE_DEPARTMENTS);
    try {
      if (associateDepartments == null || associateDepartments.size() == 0 //
          || !associateDepartments.contains(getToken().getDepartmentId())) {
        //将部门+下级部门信息放入session
        *//*List<Long> subDepartments = departmentService.findSubDepartments(getToken().getEnterpriseId(), //
            getToken().getDepartmentId()).getData();*//*
        //associateDepartments = subDepartments == null ? new ArrayList<>() : subDepartments;
        associateDepartments.add(getToken().getDepartmentId());
        getSession().setAttribute(TokenManager.SESSION_ASSOCIATE_DEPARTMENTS, associateDepartments);
      }
    } catch (InvalidSessionException e) {
      LoggerUtils.error(TokenManager.class, "getAssociateDepartments error", e);
    }
    return associateDepartments;
  }*/

  /**
   * 获取验证码，获取一次后删除.
   *
   * @return 验证码
   */
  public static String getYzm() {
    String code = (String) getSession().getAttribute("CODE");
    getSession().removeAttribute("CODE");
    return code;
  }


  /**
   * 登录.
   *
   * @param user       user
   * @param rememberMe 是否记住密码
   * @return user
   */
  public static User login(User user, Boolean rememberMe) {
    ShiroToken token = new ShiroToken(user.getPhone(), user.getPassword());
    token.setRememberMe(rememberMe);
    SecurityUtils.getSubject().login(token);
    return getToken();
  }


  /**
   * 判断是否登录.
   *
   * @return 是否登录
   */
  public static boolean isLogin() {
    User token = (User) SecurityUtils.getSubject().getPrincipal();
    if (token == null) {
      return Boolean.FALSE;
    }
    Session session = SecurityUtils.getSubject().getSession();
    SessionStatus sessionStatus = (SessionStatus) session.getAttribute(ShiroConstant.SESSION_STATUS);
    if (SessionStatus.ONLINE != sessionStatus) {
      return Boolean.FALSE;
    }
    return Boolean.TRUE;
  }

  /**
   * 退出登录.
   */
  public static void logout() {
    //清除权限
    clearNowUserAuth();
    //退出登录
    SecurityUtils.getSubject().logout();
  }

  /**
   * 清空当前用户权限信息.
   */
  public static void clearNowUserAuth() {
    try {
      /**
       * 通过ApplicationContext 从Spring容器里获取实列化对象.
       */
      realm.clearCachedAuthorizationInfo();
    } catch (Exception e) {
      LoggerUtils.warn(TokenManager.class, "clear now user auth error:" + e);
    }
  }

  /**
   * 获取当前用户的权限.
   *
   * @return 权限列表
   */
  public static ArrayList<String> getNowUserPermissions() {
    ArrayList<String> list = new ArrayList<>();
    Collection<?> permissions = realm.getPermissions();
    if (permissions != null && permissions.size() > 0) {
      for (Object obj : permissions) {
        list.add(String.valueOf(obj));
      }
    }
    return list;
  }

  /**
   * 根据用户ID获取权限.
   *
   * @param userIds userIds
   * @return 用户权限信息
   */
  public static HashMap<Long, ArrayList<String>> getUserPermissionsByUserId(Long... userIds) {
    HashMap<Long, ArrayList<String>> map = new HashMap<Long, ArrayList<String>>();

    if (null == userIds || userIds.length == 0) {
      return map;
    }
    List<SimplePrincipalCollection> result = customSessionManager.getSimplePrincipalCollectionByUserId(userIds);

    Collection<?> permissions = null;
    ArrayList<String> list = null;
    for (SimplePrincipalCollection simplePrincipalCollection : result) {
      permissions = realm.getPermissions(simplePrincipalCollection);
      list = new ArrayList<>();
      if (permissions != null && permissions.size() > 0) {
        for (Object obj : permissions) {
          list.add(String.valueOf(obj));
        }
      }
      map.put(((User) (simplePrincipalCollection.getPrimaryPrincipal())).getId(), list);
    }
    return map;
  }

  /**
   * 方法重载.
   *
   * @param userIds userIds
   */
  public static void getUserPermissionsByUserId(List<Long> userIds) {
    if (null == userIds || userIds.size() == 0) {
      return;
    }
    getUserPermissionsByUserId(userIds.toArray(new Long[0]));
  }


  /**
   * 根据UserIds清空权限信息.
   *
   * @param userIds 用户ID
   */
  public static void clearUserAuthByUserId(Long... userIds) {

    if (null == userIds || userIds.length == 0) {
      return;
    }
    List<SimplePrincipalCollection> result = customSessionManager.getSimplePrincipalCollectionByUserId(userIds);

    for (SimplePrincipalCollection simplePrincipalCollection : result) {
      realm.clearCachedAuthorizationInfo(simplePrincipalCollection);
    }
  }


  /**
   * 方法重载.
   *
   * @param userIds userIds
   */
  public static void clearUserAuthByUserId(List<Long> userIds) {
    if (null == userIds || userIds.size() == 0) {
      return;
    }
    clearUserAuthByUserId(userIds.toArray(new Long[0]));
  }

  /**
   * 判断该企业是否为新奥物流有限公司.
   *
   * @return true:是
   */
  public static boolean isEnnEnterprise() {
    //return Arrays.asList(ENN_ENTERPRISE.split(",")).contains(getEnterpriseId().toString()) ? true : false;'
    return false;
  }
}
