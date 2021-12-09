package edu.hzu.englishstudyweb.controller;


import cn.dev33.satoken.stp.StpUtil;
import edu.hzu.englishstudyweb.model.User;
import edu.hzu.englishstudyweb.service.StudySetService;
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
 * @since 2021-11-26
 */
@Controller
public class BookController {

    @Resource
    UserService userService;

    @Resource
    StudySetService studySetService;


    @RequestMapping("/book")
    public String book() {
        if (!StpUtil.isLogin()) {
            return "redirect:login";
        }
        return "book";
    }

    @ResponseBody
    @RequestMapping("/book/set")
    public String setBook(String book) {
        Result result = userService.setBook(StpUtil.getLoginIdAsInt(), book);
        studySetService.deleteAllWordByUser(StpUtil.getLoginIdAsInt());
        User user = new User();
        user.setId(StpUtil.getLoginIdAsInt());
        studySetService.addWord(user);
        if (result.isSuccess()) {
            return "alert('" + "词书修改成功，请前往学习页面学习" + "')";
        } else {
            return "alert('" + result.getMsg() + "')";
        }
    }
}
