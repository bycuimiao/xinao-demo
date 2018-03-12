package com.xinao.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

/**
 * @auther cuimiao
 * @date 2018/2/24/024  12:02
 * @Description: controller demo cuimiao+++.
 */
//@Controller
public class Hello {

    @Autowired
    private HelloService helloService;

    public Hello(){
        System.out.println("init hello");
    }

    @ResponseBody
    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public void hello(){
        List<String> list = Arrays.asList("Hello", "World!");
        list.stream().forEach(System.out::println);

    }
}
