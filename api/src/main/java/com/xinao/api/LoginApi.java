package com.xinao.api;

import com.xinao.bl.service.UserService;
import com.xinao.common.Result;
import com.xinao.common.State;
import com.xinao.entity.User;
import com.xinao.shiro.token.manager.TokenManager;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(LoginApi.class);

    @RequestMapping(value = "/" , method = RequestMethod.GET)
    @ResponseBody
    public Result<State,State> login(@RequestParam(value = "name" , required = false) String name ,
                        @RequestParam(value = "phone" , required = false) String phone ,
                        @RequestParam(value = "password" , required = true) String password){
        logger.debug("a hhhhhhhhhhhh log success");
        logger.error("error qqqqqqqqqqqqqqqqq");
        Result<State,State> result = new Result<>();
        Result<User,State> resultUser = userService.findUserByPhone(phone);
        try {
            if(State.SUCCESS.equals(resultUser.getCode())){
                User user = resultUser.getData();
                //TODO 对密码进行加密
                user = TokenManager.login(user, true);
                result.setCode(State.SUCCESS);
                result.setMessage("登录成功");
            }else {

                result.setCode(State.FAILED);
            }
        }catch (Exception e){
            //TODO
            System.out.println(e.getMessage());
            result.setCode(State.FAILED);
        }

        return result;
    }

}


//github test 01