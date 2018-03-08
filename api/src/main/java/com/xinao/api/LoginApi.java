package com.xinao.api;

import com.xinao.common.Result;
import com.xinao.common.State;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @auther cuimiao
 * @date 2018/2/25/025  17:12
 * @Description: ${todo}
 */
@Controller
@RequestMapping(value = "/login")
public class LoginApi {

    @RequestMapping(value = "/" , method = RequestMethod.POST)
    @ResponseBody
    public Result login(@RequestParam(value = "name" , required = false) String name ,
                        @RequestParam(value = "password" , required = false) String password){
        System.out.println(name);
        System.out.println(password);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name, password);
        try {
            //4、登录，即身份验证
            subject.login(token);
        } catch (AuthenticationException e) {
            //5、身份验证失败
            System.out.println("身份验证失败");
        }
        Result<String,State> result = new Result<>();
        result.setCode(State.SUCCESS);
        result.setData("cuimiao");
        return result;
    }
}
