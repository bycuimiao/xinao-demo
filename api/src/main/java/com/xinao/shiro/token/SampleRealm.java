/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.shiro.token;

import com.xinao.bl.service.PrivilegeService;
import com.xinao.bl.service.UserService;
import com.xinao.common.Result;
import com.xinao.common.State;
import com.xinao.entity.Privilege;
import com.xinao.entity.User;
import com.xinao.shiro.common.ShiroConstant;
import com.xinao.shiro.token.manager.TokenManager;
import com.xinao.shiro.util.LoggerUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public class SampleRealm extends AuthorizingRealm {
  protected static Logger logger = LoggerFactory.getLogger(SampleRealm.class);

  @Autowired
  private UserService userService;

  @Autowired
  private PrivilegeService privilegeService;

  public SampleRealm() {
    super();
  }

  /**
   * 认证信息，主要针对用户登录.
   */
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
    ShiroToken token = (ShiroToken) authcToken;
    logger.info(token.getUsername() + "进入认证方法");
    //System.out.println(token.getUsername() + token.getPswd());
    /*Result<User, State> result = userService.login(token.getUsername(), token.getPswd());
    User user = State.SUCCESS == result.getCode() ? result.getData() : null;
    if (null == user) {
      throw new AccountException("帐号或密码不正确！");
    } else if (Status.DELETED == user.getStatus()) {
      throw new DisabledAccountException("您的账号已被移除，请联系管理员！");
    } else if (Status.DISABLED == user.getStatus()) {
      //如果用户的status为禁用。那么就抛出<code>LockedAccountException</code>
      throw new LockedAccountException("帐号已经禁止登录！");
    } else {
      //更新登录时间 last login time
      user.setLastLoginTime(new Date());
      userService.updateUser(user);
    }*/
    //TODO 从持久层获取user
    Result<User, State> result = userService.login(token.getUsername(), token.getPswd());
    User user = State.SUCCESS == result.getCode() ? result.getData() : null;
    if (user == null) {
      throw new AccountException("帐号或密码不正确！");
    }
    return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
  }

  /**
   * 授权.
   */
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    User user = TokenManager.getToken();
    logger.info(user.getPhone() + "进入授权方法");
    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    //根据用户ID查询权限（permission），放入到Authorization里。
    Set<String> permissions = new TreeSet<>();
    Result<List<Privilege>, State> result = privilegeService.findPrivileges(user.getId());
    if (State.SUCCESS == result.getCode()) {
      List<Privilege> privilegeList = result.getData();
      permissions = privilegeList.stream()
          .map((privilege) -> privilege.getUrl() + "_" + privilege.getType())
          .collect(Collectors.toSet());
    }
    info.setStringPermissions(permissions);
    return info;
  }

  /**
   * 清空当前用户权限信息.
   */
  public void clearCachedAuthorizationInfo() {
    PrincipalCollection principalCollection = SecurityUtils.getSubject().getPrincipals();
    SimplePrincipalCollection principals = new SimplePrincipalCollection(principalCollection, getName());
    super.clearCachedAuthorizationInfo(principals);
  }

  /**
   * 指定principalCollection 清除.
   */
  public void clearCachedAuthorizationInfo(PrincipalCollection principalCollection) {
    SimplePrincipalCollection principals = new SimplePrincipalCollection(principalCollection, getName());
    super.clearCachedAuthorizationInfo(principals);
  }

  /**
   * 获取权限列表.
   *
   * @return 权限列表
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public Collection getPermissions() {
    PrincipalCollection principalCollection = SecurityUtils.getSubject().getPrincipals();
    SimplePrincipalCollection principals = new SimplePrincipalCollection(principalCollection, getName());
    AuthorizationInfo info = null;
    try {
      info = getAuthorizationInfo(principals);
    } catch (Exception e) {
      LoggerUtils.fmtWarn(getClass(), "获取权限失败", e);
    }
    Set permissions = new HashSet();
    if (info != null) {
      Collection<Permission> perms = info.getObjectPermissions();
      if (!CollectionUtils.isEmpty(perms)) {
        permissions.addAll(perms);
      }
      perms = resolvePermissions(info.getStringPermissions());
      if (!CollectionUtils.isEmpty(perms)) {
        permissions.addAll(perms);
      }
      perms = resolveRolePermissions(info.getRoles());
      if (!CollectionUtils.isEmpty(perms)) {
        permissions.addAll(perms);
      }
    }
    if (permissions.isEmpty()) {
      return Collections.emptySet();
    } else {
      return Collections.unmodifiableSet(permissions);
    }
  }

  /**
   * 获取制定principalCollection权限列表.
   *
   * @param principalCollection principalCollection
   * @return 权限列表
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public Collection getPermissions(PrincipalCollection principalCollection) {
    SimplePrincipalCollection principals = new SimplePrincipalCollection(principalCollection, getName());
    AuthorizationInfo info = null;
    try {
      info = getAuthorizationInfo(principals);
    } catch (Exception e) {
      LoggerUtils.fmtWarn(getClass(), "获取权限失败", e);
    }
    Set permissions = new HashSet();
    if (info != null) {
      Collection<Permission> perms = info.getObjectPermissions();
      if (!CollectionUtils.isEmpty(perms)) {
        permissions.addAll(perms);
      }
      perms = resolvePermissions(info.getStringPermissions());
      if (!CollectionUtils.isEmpty(perms)) {
        permissions.addAll(perms);
      }
      perms = resolveRolePermissions(info.getRoles());
      if (!CollectionUtils.isEmpty(perms)) {
        permissions.addAll(perms);
      }
    }
    if (permissions.isEmpty()) {
      return Collections.emptySet();
    } else {
      return Collections.unmodifiableSet(permissions);
    }
  }

  /**
   * 解析权限.
   *
   * @param stringPerms stringPerms
   * @return 权限列表
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  private Collection resolvePermissions(Collection stringPerms) {
    Collection perms = Collections.emptySet();
    PermissionResolver resolver = getPermissionResolver();
    if (resolver != null && !CollectionUtils.isEmpty(stringPerms)) {
      perms = new LinkedHashSet(stringPerms.size());
      Permission permission;
      for (Iterator iterator = stringPerms.iterator(); iterator.hasNext(); perms.add(permission)) {
        String strPermission = (String) iterator.next();
        permission = getPermissionResolver().resolvePermission(strPermission);
      }
    }
    return perms;
  }

  /**
   * 解析权限.
   *
   * @param roleNames roleNames
   * @return 权限列表
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  private Collection resolveRolePermissions(Collection roleNames) {
    Collection<? extends Object> perms = Collections.emptySet();
    RolePermissionResolver resolver = getRolePermissionResolver();
    if (resolver != null && !CollectionUtils.isEmpty(roleNames)) {
      perms = new LinkedHashSet<>(roleNames.size());
      Iterator<?> iterator = roleNames.iterator();
      do {
        if (!iterator.hasNext()) {
          break;
        }
        String roleName = (String) iterator.next();
        Collection resolved = resolver.resolvePermissionsInRole(roleName);
        if (!CollectionUtils.isEmpty(resolved)) {
          perms.addAll(resolved);
        }
      } while (true);
    }
    return perms;
  }


  /**
   * 重写是否有权限.
   *
   * @param permission permission
   * @param info       认证信息
   * @return 是否有权限
   */
  @Override
  protected boolean isPermitted(Permission permission, AuthorizationInfo info) {
    Collection<?> perms = this.getPermissions(info);
    if (perms != null && !perms.isEmpty()) {
      Iterator<?> iterator = perms.iterator();

      while (iterator.hasNext()) {
        Permission perm = (Permission) iterator.next();
        if (perm.implies(permission) || isMachter(perm, permission)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * 是否匹配权限.
   *
   * @param perm    perm
   * @param curPerm curPerm
   * @return 是否匹配
   */
  private boolean isMachter(Permission perm, Permission curPerm) {
    String source = perm.toString().replace("[", "").replace("]", "");
    String current = curPerm.toString().replace("[", "").replace("]", "");

    //不含*不需要动态匹配
    if (source.indexOf("*") == -1) {
      return false;
    }

    //没有访问方式不匹配
    if (source.indexOf(ShiroConstant.SPLIT) == -1 || current.indexOf(ShiroConstant.SPLIT) == -1) {
      return false;
    }

    String ssuffix = source.substring(source.lastIndexOf(ShiroConstant.SPLIT) + 1);
    String csuffix = current.substring(current.lastIndexOf(ShiroConstant.SPLIT) + 1);

    //访问方式不一致，不匹配
    if (!csuffix.equals(ssuffix)) {
      return false;
    }

    String sprefix = source.substring(0, source.lastIndexOf(ShiroConstant.SPLIT));
    String cprefix = current.substring(0, current.lastIndexOf(ShiroConstant.SPLIT));

    String[] surlarr = sprefix.split("/");
    String[] curlarr = cprefix.split("/");

    //分割url不一致,不匹配
    if (surlarr.length != curlarr.length) {
      return false;
    }

    for (int i = 0; i < surlarr.length; i++) {
      //参数不一样,并且不是动态参数匹配
      if (!surlarr[i].equals(curlarr[i]) && !"*".equals(surlarr[i])) {
        return false;
      }
    }

    return true;
  }
}
