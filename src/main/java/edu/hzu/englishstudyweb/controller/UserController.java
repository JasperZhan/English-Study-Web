package edu.hzu.englishstudyweb.controller;


import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import edu.hzu.englishstudyweb.model.Code;
import edu.hzu.englishstudyweb.model.User;
import edu.hzu.englishstudyweb.service.SmsService;
import edu.hzu.englishstudyweb.service.UserService;
import edu.hzu.englishstudyweb.util.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;


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

    @Resource
    SmsService smsService;

    /**
     * 登录页面
     * @author Jasper Zhan
     * @date 2021/12/4 9:56
     * @return java.lang.String
     */
    @RequestMapping("login")
    public String login() {
        return "login";
    }

    /**
     * 登录验证
     * @author Jasper Zhan
     * @date 2021/11/27 10:47
     * @param tell 登录手机号
     * @param password 登录密码
     * @return java.lang.String
     */
    @ResponseBody
    @RequestMapping("login/check")
    public String loginCheck(String tell, String password){
        User user = new User();
        user.setTell(tell);
        user.setPassword(SaSecureUtil.md5(password));

        Result result = userService.login(user);
        if (result.isSuccess()) {
            user = (User) result.getData();
            StpUtil.login(user.getId());
            return "location.href='/study'";
        } else {
            return "alert('" + result.getMsg() + "')";
        }
    }


    @RequestMapping("register")
    public String register(String tell, String password) {
//        User user = new User();
//        user.setTell(tell);
//        user.setPassword(SaSecureUtil.md5(password));
//        Result result = userService.register(user);
//        if (result.isSuccess()) {
//            return "注册成功";
//        } else {
//            return "alert('" + result.getMsg() + "')";
//        }
        return "register";
    }

    @ResponseBody
    @RequestMapping("register/code")
    public String registerCheck(String tell, HttpSession session) {

        // 保存申请发送验证码时的手机号或邮箱号，作为注册时的验证
        session.setAttribute("tell", tell);

        // 保存发送验证码服务生成的验证码结果
        Result result = smsService.sendCode(tell);

        if (!result.isSuccess()) {
            return result.getMsg();
        }

        Code code = (Code)result.getData();

        // 保存发送的验证码，以及发送验证码的时间
        session.setAttribute("code", code.getCode());
        session.setAttribute("sendTime", String.valueOf(code.getCurrentTime()));
        return "验证码已发送，请注意查看";
    }

    @ResponseBody
    @RequestMapping("/register/check")
    public String registerCheck(String tell, String password, String code, HttpSession session) {

        try {
            // 防止用户发送验证码后修改号码
            if (!tell.equals(session.getAttribute("tell").toString())) {
                return "alert('注册失败，手机号码与发送验证码的手机号不匹对')";
            }

            // 验证码与发送的验证码不一样
            if (!code.equals(session.getAttribute("code").toString())) {
                return "alert('验证码错误，请重新输入')";
            }

            long sendTime = Long.parseLong(session.getAttribute("sendTime").toString());
            if (!((System.currentTimeMillis() / 1000) - sendTime < 180)) {
                session.removeAttribute("code");
                return "alert('验证码已过期，请重新获取')";
            }
        } catch (NullPointerException e) {
            return "alert('session 为空')";
        }


        // 判断密码格式
        if (password == null)
            return "alert('密码不能为空')";
        if (!password.matches("^\\w{8,20}$"))
            return "alert('密码长度需在8-20位之间')";
        if (!password.matches(".*\\d+.*"))
            return "alert('密码需包含数字')";
        if (!password.matches(".*[a-zA-Z]+.*"))
            return "alert('密码需包含字母')";

        User user = new User();
        user.setTell(tell);
        user.setPassword(SaSecureUtil.md5(password));

        Result result = userService.register(user);

        if (!result.isSuccess())
            return "alert('"+result.getMsg()+"')";

        // 注册完成
        // 清楚session
        // 反回登录页面
        session.invalidate();
        return "location.href='/user/login'";
    }

    @RequestMapping("/change_password")
    public String changePassword() {
        return "changePassword";
    }

    @ResponseBody
    @RequestMapping("/change_password/check")
    public String change(String tell, String password, String code, HttpSession session) {

        try {
            // 防止用户发送验证码后修改号码
            if (!tell.equals(session.getAttribute("tell").toString())) {
                return "alert('注册失败，手机号码与发送验证码的手机号不匹对')";
            }

            // 验证码与发送的验证码不一样
            if (!code.equals(session.getAttribute("code").toString())) {
                return "alert('验证码错误，请重新输入')";
            }

            long sendTime = Long.parseLong(session.getAttribute("sendTime").toString());
            if (!((System.currentTimeMillis() / 1000) - sendTime < 180)) {
                session.removeAttribute("code");
                return "alert('验证码已过期，请重新获取')";
            }
        } catch (NullPointerException e) {
            return "alert('session 为空')";
        }


        // 判断密码格式
        if (password == null)
            return "alert('密码不能为空')";
        if (!password.matches("^\\w{8,20}$"))
            return "alert('密码长度需在8-20位之间')";
        if (!password.matches(".*\\d+.*"))
            return "alert('密码需包含数字')";
        if (!password.matches(".*[a-zA-Z]+.*"))
            return "alert('密码需包含字母')";

        User user = new User();
        user.setTell(tell);
        user.setPassword(SaSecureUtil.md5(password));

        Result result = userService.updateUserOfPw(user);

        if (!result.isSuccess())
            return "alert('"+result.getMsg()+"')";

        // 注册完成
        // 清楚session
        // 反回登录页面
        session.invalidate();
        if (StpUtil.isLogin()) {
            StpUtil.logout();
        }
        return "location.href='/user/login'";
    }

    /**
     * 注销登录
     * @author Jasper Zhan
     * @date 2021/11/27 10:55
     * @return java.lang.String
     */
    @RequestMapping("/logout")
    public String logout() {
        StpUtil.logout();
        return "redirect:login";
    }
}
