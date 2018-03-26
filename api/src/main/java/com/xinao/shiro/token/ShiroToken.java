/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.shiro.token;

import org.apache.shiro.authc.UsernamePasswordToken;

import java.io.Serializable;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public class ShiroToken extends UsernamePasswordToken implements Serializable {
  private static final long serialVersionUID = 1013949761493503708L;

  public ShiroToken(String username, String pswd) {
    super(username, pswd);
    this.pswd = pswd;
  }

  /**
   * 登录密码[字符串类型] 因为父类是char[] ].
   **/
  private String pswd;

  public String getPswd() {
    return pswd;
  }


  public void setPswd(String pswd) {
    this.pswd = pswd;
  }

}
