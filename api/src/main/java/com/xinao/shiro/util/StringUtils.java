/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.shiro.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public class StringUtils extends org.apache.commons.lang.StringUtils {
  protected static Logger logger = LoggerFactory.getLogger(StringUtils.class);

  /**
   * 一次性判断多个或单个对象为空.
   *
   * @param objects objects
   * @return 只要有一个元素为Blank，则返回true
   */
  public static boolean isBlank(Object... objects) {
    Boolean result = false;
    for (Object object : objects) {
      if (null == object || "".equals(object.toString().trim())
          || "null".equals(object.toString().trim())) {
        result = true;
        break;
      }
    }
    return result;
  }

  public static boolean isBlank(String... objects) {
    Object[] object = objects;
    return isBlank(object);
  }

  public static boolean isBlank(String str) {
    Object object = str;
    return isBlank(object);
  }

  public static boolean isNotBlank(String str) {
    Object object = str;
    return !isBlank(object);
  }


  /**
   * 一次性判断多个或单个对象不为空.
   *
   * @param objects objects
   * @return 只要有一个元素不为Blank，则返回true
   */
  public static boolean isNotBlank(Object... objects) {
    return !isBlank(objects);
  }

  public static boolean isNotBlank(String... objects) {
    Object[] object = objects;
    return !isBlank(object);
  }


  /**
   * 获取随机数.
   *
   * @param length length
   * @return 指定长度的随机数
   */
  public static String getRandom(int length) {
    String val = "";
    Random random = new Random();
    for (int i = 0; i < length; i++) {
      // 输出字母还是数字
      String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
      // 字符串
      if ("char".equalsIgnoreCase(charOrNum)) {
        // 取得大写字母还是小写字母
        int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
        val += (char) (choice + random.nextInt(26));
      } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
        val += String.valueOf(random.nextInt(10));
      }
    }
    return val.toLowerCase();
  }


  /**
   * 判断一个字符串在数组中存在几个.
   *
   * @param baseStr baseStr
   * @param strings strings
   * @return 字符串在数组中存在几个
   */
  public static int indexOf(String baseStr, String[] strings) {

    if (null == baseStr || baseStr.length() == 0 || null == strings) {
      return 0;
    }

    int num = 0;
    for (String string : strings) {
      boolean result = baseStr.equals(string);
      num = result ? ++num : num;
    }
    return num;
  }

  /**
   * 判断一个字符串是否为JSONObject,是返回JSONObject,不是返回null.
   *
   * @param args args
   * @return 字符串是否为JSONObject
   */
  public static JSONObject isJsonObject(String args) {
    JSONObject result = null;
    if (isBlank(args)) {
      return result;
    }
    try {
      return JSONObject.parseObject(args.trim());
    } catch (Exception e) {
      return result;
    }
  }

  /**
   * 判断一个字符串是否为JSONArray,是返回JSONArray,不是返回null.
   *
   * @param args args
   * @return 字符串是否为JSONArray
   */
  public static JSONArray isJsonArray(Object args) {
    JSONArray result = new JSONArray();
    if (isBlank(args)) {
      return null;
    }
    if (args instanceof JSONArray) {
      JSONArray arr = (JSONArray) args;
      for (Object json : arr) {
        if (json != null && json instanceof JSONObject) {
          result.add(json);
          continue;
        } else {
          result.add(JSONObject.toJSON(json));
        }
      }
      return result;
    } else {
      return null;
    }

  }

  public static String trimToEmpty(Object str) {
    return (isBlank(str) ? "" : str.toString().trim());
  }

  /**
   * 将 Strig  进行 BASE64 编码.
   *
   * @param str [要编码的字符串]
   * @param bf  [true|false,true:去掉结尾补充的'=',false:不做处理]
   * @return String  进行 BASE64 编码
   */
  public static String getBase64(String str, boolean... bf) {
    if (StringUtils.isBlank(str)) {
      return null;
    }
    String base64 = Base64.encodeBase64String(str.getBytes());
    //去掉 '='
    if (isBlank(bf) && bf[0]) {
      base64 = base64.replaceAll("=", "");
    }
    return base64;
  }

  /**
   * 将 BASE64 编码的字符串 source 进行解码.
   *
   * @param source 要解码的字符串
   * @return String  进行 BASE64 编码
   **/
  public static String getStrByBase64(String source) {
    if (isBlank(source)) {
      return "";
    }
    try {
      byte[] bytes = Base64.decodeBase64(source);
      return new String(bytes);
    } catch (Exception e) {
      return "";
    }
  }

  /**
   * 把Map转换成get请求参数类型.
   *
   * @param map map
   * @return Map转换成get请求参数类型
   */
  public static String mapToGet(Map<? extends Object, ? extends Object> map) {
    String result = "";
    if (map == null || map.size() == 0) {
      return result;
    }
    Set<? extends Object> keys = map.keySet();
    for (Object key : keys) {
      result += ((String) key + "=" + (String) map.get(key) + "&");
    }

    return isBlank(result) ? result : result.substring(0, result.length() - 1);
  }

  /**
   * 把一串参数字符串转换成Map.
   *
   * @param args args
   * @return 参数字符串, 转换成Map
   */
  public static Map<String, ? extends Object> getToMap(String args) {
    if (isBlank(args)) {
      return null;
    }
    args = args.trim();
    //如果是?开头,把?去掉
    if (args.startsWith("?")) {
      args = args.substring(1, args.length());
    }
    String[] argsArray = args.split("&");

    Map<String, Object> result = new HashMap<String, Object>();
    for (String ag : argsArray) {
      if (!isBlank(ag) && ag.indexOf("=") > 0) {

        String[] keyValue = ag.split("=");
        //如果value或者key值里包含 "="号,以第一个"="号为主 ,如  name=0=3  转换后,{"name":"0=3"}, 如果不满足需求,请勿修改,自行解决.

        String key = keyValue[0];
        String value = "";
        for (int i = 1; i < keyValue.length; i++) {
          value += keyValue[i] + "=";
        }
        value = value.length() > 0 ? value.substring(0, value.length() - 1) : value;
        result.put(key, value);

      }
    }

    return result;
  }

  /**
   * 转换成Unicode.
   *
   * @param str str
   * @return 转换成Unicode
   */
  public static String toUnicode(String str) {
    String[] as = new String[str.length()];
    String s1 = "";
    for (int i = 0; i < str.length(); i++) {
      int val = str.charAt(i);
      if (val >= 19968 && val <= 171941) {
        as[i] = Integer.toHexString(str.charAt(i) & 0xffff);
        s1 = s1 + "\\u" + as[i];
      } else {
        s1 = s1 + str.charAt(i);
      }
    }
    return s1;
  }

  /**
   * 合并数据.
   *
   * @param value value
   * @return 合并数据
   */
  public static String merge(Object... value) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < value.length; i++) {
      sb.append(value[i]);
    }
    return sb.toString();
  }

  /**
   * 字符串转urlcode.
   *
   * @param value value
   * @return 字符串转urlcode
   */
  public static String strToUrlcode(String value) {
    try {
      value = java.net.URLEncoder.encode(value, "utf-8");
      return value;
    } catch (UnsupportedEncodingException e) {
      logger.error("字符串转换为URLCode失败,value:" + value, e);
      e.printStackTrace();
      return null;
    }
  }

  /**
   * urlcode转字符串.
   *
   * @param value value
   * @return urlcode转字符串
   */
  public static String urlcodeToStr(String value) {
    try {
      value = java.net.URLDecoder.decode(value, "utf-8");
      return value;
    } catch (UnsupportedEncodingException e) {
      logger.error("URLCode转换为字符串失败;value:" + value, e);
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 判断字符串是否包含汉字.
   *
   * @param txt txt
   * @return 是否包含汉字
   */
  public static Boolean containsCn(String txt) {
    if (isBlank(txt)) {
      return false;
    }
    return txt.getBytes().length == txt.length() ? false : true;
  }

  /**
   * 去掉HTML代码.
   *
   * @param news news
   * @return 去掉HTML代码
   */
  public static String removeHtml(String news) {
    String sval = news.replaceAll("amp;", "").replaceAll("<", "<").replaceAll(">", ">");

    Pattern pattern = Pattern.compile("<(span)?\\sstyle.*?style>|(span)?\\sstyle=.*?>", Pattern.DOTALL);
    Matcher matcher = pattern.matcher(sval);
    String str = matcher.replaceAll("");

    Pattern pattern2 = Pattern.compile("(<[^>]+>)", Pattern.DOTALL);
    Matcher matcher2 = pattern2.matcher(str);
    String strhttp = matcher2.replaceAll(" ");


    String regEx = "(((http|https|ftp)(\\s)*((\\:)|：))(\\s)*(//|//)(\\s)*)?"
        + "([\\sa-zA-Z0-9(\\.|．)(\\s)*\\-]+((\\:)|(:)[\\sa-zA-Z0-9(\\.|．)&%\\$\\-]+)*@(\\s)*)?"
        + "("
        + "(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])"
        + "(\\.|．)(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)"
        + "(\\.|．)(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)"
        + "(\\.|．)(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])"
        + "|([\\sa-zA-Z0-9\\-]+(\\.|．)(\\s)*)*[\\sa-zA-Z0-9\\-]+(\\.|．)(\\s)*[\\sa-zA-Z]*"
        + ")"
        + "((\\s)*(\\:)|(：)(\\s)*[0-9]+)?"
        + "(/(\\s)*[^/][\\sa-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&%\\$\\=~_\\-@]*)*";
    Pattern p1 = Pattern.compile(regEx, Pattern.DOTALL);
    Matcher matchhttp = p1.matcher(strhttp);
    String strnew = matchhttp.replaceAll("").replaceAll("(if[\\s]*\\(|else|elseif[\\s]*\\().*?;", " ");


    Pattern patterncomma = Pattern.compile("(&[^;]+;)", Pattern.DOTALL);
    Matcher matchercomma = patterncomma.matcher(strnew);
    String strout = matchercomma.replaceAll(" ");
    String answer = strout.replaceAll("[\\pP‘’“”]", " ")
        .replaceAll("\r", " ").replaceAll("\n", " ")
        .replaceAll("\\s", " ").replaceAll("　", "");


    return answer;
  }

  /**
   * 把数组的空数据去掉.
   *
   * @param array array
   * @return 数组的空数据去掉
   */
  public static List<String> array2Empty(String[] array) {
    List<String> list = new ArrayList<String>();
    for (String string : array) {
      if (StringUtils.isNotBlank(string)) {
        list.add(string);
      }
    }
    return list;
  }

  /**
   * 把数组转换成set.
   *
   * @param <T>   泛型类型
   * @param array array
   * @return 数组转换成set
   */
  public static <T> Set<T> array2Set(T[] array) {
    Set<T> set = new TreeSet<T>();
    for (T id : array) {
      if (null != id) {
        set.add(id);
      }
    }
    return set;
  }

  /**
   * serializable toString.
   *
   * @param serializable serializable
   * @return serializable toString
   */
  public static String toString(Serializable serializable) {
    if (null == serializable) {
      return null;
    }
    try {
      return (String) serializable;
    } catch (Exception e) {
      return serializable.toString();
    }
  }

}
