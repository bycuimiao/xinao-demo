/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.shiro.util;

import com.xinao.entity.User;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Date;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public class EncryptUtil {

  private static final Logger log = LoggerFactory.getLogger(EncryptUtil.class);
  /**
   * 加工密码，和登录一致.
   *
   * @param user user
   * @return user对象
   */
  public static User md5Pswd(User user) {
    //密码为 phone + '#' + pswd，然后MD5
    user.setPassword(md5Pswd(user.getCreateTime(), user.getPassword()));
    return user;
  }

  /**
   * 字符串返回值.
   *
   * @param date     date
   * @param password password
   * @return 加密密码
   */
  public static String md5Pswd(Date date, String password) {
    return getMD5(String.format("%s#%s", DateFormatUtils.format(date, "yyyyMMddhhmmss"), password));
  }

  /**
   * MD5 加密.
   *
   * @param str str
   * @return MD5加密str
   */
  public static String getMD5(String str) {
    if(log.isDebugEnabled()){
      log.debug("cleartext -- " + str);
    }
    MessageDigest messageDigest = null;
    try {
      messageDigest = MessageDigest.getInstance("MD5");
      messageDigest.reset();
      messageDigest.update(str.getBytes("UTF-8"));
    } catch (Exception e) {
      LoggerUtils.fmtError(EncryptUtil.class, e, "MD5转换异常！message：%s", e.getMessage());
    }

    byte[] byteArray = messageDigest.digest();
    StringBuffer md5StrBuff = new StringBuffer();
    for (int i = 0; i < byteArray.length; i++) {
      if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
        md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
      } else {
        md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
      }
    }
    return md5StrBuff.toString();
  }
}
