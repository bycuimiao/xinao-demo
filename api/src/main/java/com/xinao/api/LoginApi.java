package com.xinao.api;

import com.xinao.common.Result;
import com.xinao.common.State;
import com.xinao.common.model.UUser;
import com.xinao.shiro.token.manager.TokenManager;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
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

    @RequestMapping(value = "/" , method = RequestMethod.GET)
    @ResponseBody
    public Result login(@RequestParam(value = "name" , required = false) String name ,
                        @RequestParam(value = "password" , required = false) String password){
        System.out.println(name);
        System.out.println(password);
        /*Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name, password);*/
        try {
            //4、登录，即身份验证
            //subject.login(token);
            UUser uUser = new UUser();
            uUser.setEmail(name);
            uUser.setPswd(password);
            TokenManager.login(uUser,true);
            /*System.out.println("SESSION ID = " + SecurityUtils.getSubject().getSession().getId());
            System.out.println("用户名：" + SecurityUtils.getSubject().getPrincipal());
            System.out.println("HOST：" + SecurityUtils.getSubject().getSession().getHost());
            System.out.println("TIMEOUT ：" + SecurityUtils.getSubject().getSession().getTimeout());
            System.out.println("START：" + SecurityUtils.getSubject().getSession().getStartTimestamp());
            System.out.println("LAST：" + SecurityUtils.getSubject().getSession().getLastAccessTime());*/
        } catch (AuthenticationException e) {
            //5、身份验证失败
            System.out.println("身份验证失败");
        }
        Result<String,State> result = new Result<>();
        UUser uUser = TokenManager.getToken();
        result.setCode(State.SUCCESS);
        result.setData(uUser.getEmail());
        return result;
    }

}
