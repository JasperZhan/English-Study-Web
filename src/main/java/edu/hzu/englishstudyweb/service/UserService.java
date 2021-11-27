package edu.hzu.englishstudyweb.service;

import edu.hzu.englishstudyweb.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.hzu.englishstudyweb.util.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jasper Zhan
 * @since 2021-11-27
 */
public interface UserService extends IService<User> {
    /**
     * @author Jasper Zhan
     * @date 2021/11/26 22:45
     * @param user 登录用户实体类
     * @return edu.hzu.englishstudyweb.util.Result
     */
    public Result login(User user);

    /**
     * @author Jasper Zhan
     * @date 2021/11/26 22:47
     * @param user 注册用户实体类
     * @return edu.hzu.englishstudyweb.util.Result
     */
    public Result register(User user);

}
