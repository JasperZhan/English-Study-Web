package edu.hzu.englishstudyweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jasper Zhan
 * @date 2021年12月04日 10:18
 */
@Controller
public class mainController {

    @RequestMapping("/")
    public String main() {
        return "redirect:study";
    }
}
