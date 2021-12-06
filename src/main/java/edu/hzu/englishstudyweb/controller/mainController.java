package edu.hzu.englishstudyweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Jasper Zhan
 * @date 2021年12月04日 10:18
 */
@Controller
public class mainController {
    @RequestMapping("/")
    public String main() {
        return "study";
    }
}
