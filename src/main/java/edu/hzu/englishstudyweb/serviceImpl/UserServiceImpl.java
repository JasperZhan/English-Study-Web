package edu.hzu.englishstudyweb.serviceImpl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import edu.hzu.englishstudyweb.model.User;
import edu.hzu.englishstudyweb.mapper.UserMapper;
import edu.hzu.englishstudyweb.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hzu.englishstudyweb.util.Result;
import edu.hzu.englishstudyweb.util.ResultCode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jasper Zhan
 * @since 2021-11-27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private RedisTemplate redisTemplate;

    private static final String USER_LIST = "user_list";

    private Collection<User> userCollection;
    private Map<String, User> userMap;

    /**
     * 登录服务
     * @author Jasper Zhan
     * @date 2021/11/27 8:30
     * @param user 用户实体
     * @return edu.hzu.englishstudyweb.util.Result
     */
    @Override
    public Result login(User user) {

        User queryUser;

        try {
            // 从缓存中查询用户数据
            userMap = redisTemplate.opsForHash().entries(USER_LIST);

            // 如果从缓存中查询不到用户数据
            if (CollectionUtils.isEmpty(userMap)) {
                System.out.println("从数据库中查询");
                // 从数据库中查询用户数据
                userCollection = list();
                userMap = convertToMap(userCollection);
                redisTemplate.opsForHash().putAll(USER_LIST, userMap);
                // 缓存过期时间为一小时
                redisTemplate.expire(USER_LIST, 3600000, TimeUnit.MILLISECONDS);
            }
            System.out.println("从缓存中读取");
            queryUser = userMap.get(user.getTell());
            if (queryUser == null) {
                return Result.failure(ResultCode.FAILURE_OF_QUERY_NULL);
            } else {
                if (queryUser.getPassword().equals(user.getPassword())) {
                    return Result.success(queryUser);
                } else {
                    return Result.failure(ResultCode.FAILURE);
                }
            }
        } catch (Exception e) {
            System.out.println("redis查询失败"+e);
            return Result.failure(ResultCode.FAILURE);
        }
    }

    /**
     * 注册服务
     * @author Jasper Zhan
     * @date 2021/11/27 8:31
     * @param user 用户实体
     * @return edu.hzu.englishstudyweb.util.Result
     */
    @Override
    public Result register(User user) {
        if (user.getTell() == null) {
            return Result.failure(ResultCode.FAILURE);
        }
        if (user.getPassword() == null) {
            return Result.failure(ResultCode.FAILURE);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tell", user.getTell()).last("LIMIT 1");
        User queryUser = getOne(queryWrapper);
        if (queryUser == null) {
            return Result.failure(ResultCode.FAILURE);
        }
        save(user);
        try {
            userCollection = list();
            userMap = convertToMap(userCollection);
            redisTemplate.opsForHash().putAll(USER_LIST, userMap);
            // 缓存过期时间为一小时
            redisTemplate.expire(USER_LIST, 3600000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            System.out.println("redis更新失败"+e);
            return Result.failure(ResultCode.FAILURE);
        }
        return Result.success(ResultCode.SUCCESS);
    }


    private Map<String, User> convertToMap(Collection<User> userCollection) {
        if (CollectionUtils.isEmpty(userCollection)) {
            return Collections.EMPTY_MAP;
        }
        Map<String, User> userMap = new HashMap<>(userCollection.size());
        for (User user: userCollection
             ) {
            userMap.put(user.getTell(), user);
        }
        return userMap;
    }

    @Override
    public Result setBook(Integer userId, String book) {

        User user = getById(userId);
        if (user == null) {
            return Result.failure(ResultCode.FAILURE_OF_QUERY_NULL);
        }
        user.setBook(book);
        updateById(user);
        return Result.success();
    }

    @Override
    public Result updateUserOfPw(User user) {
        if (user.getTell() == null) {
            return Result.failure("手机号为空");
        }
        if (user.getPassword() == null) {
            return Result.failure("密码为空");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tell", user.getTell()).last("LIMIT 1");
        User queryUser = getOne(queryWrapper);
        if (queryUser == null) {
            return Result.failure("查询结果为空");
        }
        queryUser.setPassword(user.getPassword());
        updateById(queryUser);
        try {
            userCollection = list();
            userMap = convertToMap(userCollection);
            redisTemplate.opsForHash().putAll(USER_LIST, userMap);
            // 缓存过期时间为一小时
            redisTemplate.expire(USER_LIST, 3600000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            System.out.println("redis更新失败"+e);
            return Result.failure("redis更新失败"+e);
        }
        return Result.success(ResultCode.SUCCESS);
    }
}
