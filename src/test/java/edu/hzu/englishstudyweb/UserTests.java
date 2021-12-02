package edu.hzu.englishstudyweb;

import cn.dev33.satoken.SaManager;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.hzu.englishstudyweb.model.User;
import edu.hzu.englishstudyweb.model.Word;
import edu.hzu.englishstudyweb.service.StudySetService;
import edu.hzu.englishstudyweb.service.UserService;
import edu.hzu.englishstudyweb.service.WordService;
import edu.hzu.englishstudyweb.util.Result;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class UserTests {

    @Resource
    UserService userService;

    @Resource
    WordService wordService;

    @Test
    void contextLoads() {
        System.out.println("启动成功：Sa-Token配置如下：" + SaManager.getConfig());
    }

    /**
     * 登录单元测试
     * @author Jasper Zhan
     * @date 2021/11/27 8:26
     */
    @Test
    void loginTest() {
        User user = new User();
        user.setTell("12312345678");
        user.setPassword("123456");
        Result result = userService.login(user);
        System.out.println(result.getCode());
    }

    /**
     * 注册单元测试
     * @author Jasper Zhan
     * @date 2021/11/27 8:27
     */
    @Test
    void registerTest() {
        User user = new User();
        user.setTell("231234567");
        user.setPassword("123456");
        Result result = userService.register(user);
        System.out.println(result.getCode());
    }


}
