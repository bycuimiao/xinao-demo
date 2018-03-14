package com.xinao.api;

import com.xinao.common.Result;
import com.xinao.common.State;
import com.xinao.common.model.UUser;
import com.xinao.shiro.token.manager.TokenManager;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.session.Session;
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
        UUser uUser = TokenManager.getToken();
        result.setCode(State.SUCCESS);
        result.setData(uUser.getEmail());
        Session session = TokenManager.getSession();
        System.out.println("session id === " + session.getId());
        return result;
    }
}
