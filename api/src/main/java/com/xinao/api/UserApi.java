package com.xinao.api;

import com.xinao.common.Result;
import com.xinao.common.State;
import com.xinao.entity.User;
import com.xinao.shiro.token.manager.TokenManager;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @auther cuimiao
 * @date 2018/3/14/014  23:40
 * @Description: ${todo}
 */
@Controller
@RequestMapping(value = "/user")
public class UserApi {
    @RequestMapping(value = "/" , method = RequestMethod.GET)
    @ResponseBody
    public Result list(){
        Result<String,State> result = new Result<>();
        //UUser uUser = TokenManager.getToken();
        /*Subject subject = SecurityUtils.getSubject();
        String name = (String) subject.getPrincipal();
        System.out.println(name);
        String sessionId = (String) subject.getSession().getId();
        System.out.println(sessionId);
        result.setCode(State.SUCCESS);*/
        User user = TokenManager.getToken();
        System.out.println(user.getName());
        return result;
    }
}
