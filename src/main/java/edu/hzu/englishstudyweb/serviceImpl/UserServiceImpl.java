package edu.hzu.englishstudyweb.serviceImpl;

import edu.hzu.englishstudyweb.model.User;
import edu.hzu.englishstudyweb.mapper.UserMapper;
import edu.hzu.englishstudyweb.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jasper Zhan
 * @since 2021-11-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
