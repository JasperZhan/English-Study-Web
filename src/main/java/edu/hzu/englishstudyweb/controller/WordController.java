package edu.hzu.englishstudyweb.controller;


import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import edu.hzu.englishstudyweb.model.User;
import edu.hzu.englishstudyweb.model.Word;
import edu.hzu.englishstudyweb.service.WordService;
import edu.hzu.englishstudyweb.util.Result;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Jasper Zhan
 * @since 2021-11-26
 */
@RestController
@RequestMapping("/englishstudyweb/word")
public class WordController {

    @Resource
    public  WordService wordService;


    public String Word() {
        if (StpUtil.isLogin()) {
            return "englishstudyweb/word";
        } else {
            return "login";
        }
    }



}
