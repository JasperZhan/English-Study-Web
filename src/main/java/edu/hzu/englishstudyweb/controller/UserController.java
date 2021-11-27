package edu.hzu.englishstudyweb.controller;


import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import edu.hzu.englishstudyweb.model.User;
import edu.hzu.englishstudyweb.service.UserService;
import edu.hzu.englishstudyweb.util.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Jasper Zhan
 * @since 2021-11-27
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    @Resource
    UserService userService;

    /**
     * 登录
     * @author Jasper Zhan
     * @date 2021/11/27 10:47
     * @param tell 登录手机号
     * @param password 登录密码
     * @return java.lang.String
     */
    @ResponseBody
    @RequestMapping("login")
    public String login(String tell, String password){
        User user = new User();
        user.setTell(tell);
        user.setPassword(SaSecureUtil.md5(password));

        Result result = userService.login(user);
        if (result.isSuccess()) {
            user = (User) result.getData();
            StpUtil.login(user.getId());
            return "登录成功";
        } else {
            return "alert('" + result.getMsg() + "')";
        }
    }

    @ResponseBody
    @RequestMapping("register")
    public String register(String tell, String password) {
        User user = new User();
        user.setTell(tell);
        user.setPassword(SaSecureUtil.md5(password));
        Result result = userService.register(user);
        if (result.isSuccess()) {
            return "注册成功";
        } else {
            return "alert('" + result.getMsg() + "')";
        }
    }

    /**
     * 注销登录
     * @author Jasper Zhan
     * @date 2021/11/27 10:55
     * @return java.lang.String
     */
    @ResponseBody
    @RequestMapping("logout")
    public String logout() {
        StpUtil.logout();
        return "注销账号";
    }
}
