/*
 * Created by cuimiao on 2018/3/27.
 */

package com.xinao.hello;

import org.springframework.beans.BeanUtils;

/**
 * @author cuimiao
 * @version 0.0.1
 * @Description:
 * @since 0.0.1 2018-03-27
 */
public class Temp {
  public static void main(String[] args) {
    AClass aClass = new AClass();
    aClass.setName("cuimiao");
    aClass.setPassword("123456");
    BClass bClass = new BClass();
    BeanUtils.copyProperties(aClass, bClass);
    System.out.println(bClass.getName());
  }
}