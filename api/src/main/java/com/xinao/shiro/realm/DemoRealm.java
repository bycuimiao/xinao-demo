package com.xinao.shiro.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.Realm;

/**
 * @auther cuimiao
 * @date 2018/3/8/008  22:20
 * @Description: ${todo}
 */
public class DemoRealm implements Realm {
    @Override
    public String getName() {
        return "DemoRealm";
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String userName = (String) token.getPrincipal();
        String password = new String((char[])token.getCredentials()); //得到密码
        if (!"cuimiao".equals(userName)) {
            throw new UnknownAccountException(); // 如果用户名错误
        }
        if (!"123456".equals(password)) {
            throw new IncorrectCredentialsException(); // 如果密码错误
        }
        // 如果身份认证验证成功，返回一个AuthenticationInfo实现；
        return new SimpleAuthenticationInfo(userName, password, getName());
    }
}
