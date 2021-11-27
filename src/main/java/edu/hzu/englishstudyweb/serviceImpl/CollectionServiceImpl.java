package edu.hzu.englishstudyweb.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.hzu.englishstudyweb.model.Collection;
import edu.hzu.englishstudyweb.mapper.CollectionMapper;
import edu.hzu.englishstudyweb.model.User;
import edu.hzu.englishstudyweb.model.Word;
import edu.hzu.englishstudyweb.service.CollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hzu.englishstudyweb.util.Result;
import edu.hzu.englishstudyweb.util.ResultCode;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jasper Zhan
 * @since 2021-11-26
 */
@Service
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper, Collection> implements CollectionService {

    Collection queryCollection;
    /**
     * 添加单词收藏
     * @param collection 收藏实体类
     * @return edu.hzu.englishstudyweb.util.Result
     * @author Jasper Zhan
     * @date 2021/11/27 12:14
     */
    @Override
    public Result addWord(Collection collection) {

        if (collection.getUserId() == null && collection.getWordId() == null) {
            return Result.failure(ResultCode.FAILURE);
        }
        Result result = isWordExist(collection);
        // 单词已存在
        if (result.isSuccess()) {
            return Result.failure(ResultCode.FAILURE);
        }
        if (save(collection)) {
            return Result.success(ResultCode.SUCCESS);
        };
        return Result.failure(ResultCode.FAILURE);
    }

    /**
     * 删除单词收藏
     * @param collection 收藏实体类
     * @return edu.hzu.englishstudyweb.util.Result
     * @author Jasper Zhan
     * @date 2021/11/27 12:14
     */
    @Override
    public Result deleteWord(Collection collection) {
        Result result = isWordExist(collection);
        // 单词不存在
        if (!result.isSuccess()) {
            return Result.failure(ResultCode.FAILURE);
        }
        queryCollection = (Collection) result.getData();
        if (removeById(queryCollection.getId())) {
            return Result.success(ResultCode.SUCCESS);
        };
        return Result.failure(ResultCode.FAILURE);
    }

    /**
     * 收藏的单词是否已存在
     * @param collection 收藏实体类
     * @return edu.hzu.englishstudyweb.util.Result
     * @author Jasper Zhan
     * @date 2021/11/27 12:31
     */
    @Override
    public Result isWordExist(Collection collection) {
        QueryWrapper<Collection> queryWrapper = new QueryWrapper();

        if (collection.getId() == null) {
            if (collection.getUserId() == null || collection.getWordId() == null) {
                return Result.failure(ResultCode.FAILURE);
            }
            queryWrapper.eq("user_id", collection.getUserId());
            queryWrapper.eq("word_id", collection.getWordId());
            queryWrapper.last("LIMIT 1");
        } else {
            queryWrapper.eq("id", collection.getId());
        }

        queryCollection = getOne(queryWrapper);

        if (queryCollection == null) {
            return Result.failure(ResultCode.FAILURE);
        } else {
            return Result.success(ResultCode.SUCCESS, queryCollection);
        }
    }

    /**
     * @param current 当前查询分页索引
     * @param number  查询结果条数
     * @param user    用户类
     * @return edu.hzu.englishstudyweb.util.Result>
     * @author Jasper Zhan
     * @date 2021/11/27 17:00
     */
    @Override
    public Result showCollectionPage(int current, int number, User user) {
        if (user.getId() == null) {
            return Result.failure(ResultCode.FAILURE);
        }
        Page<Word> page = new Page<Word>(current, number);
        page = page.setRecords(this.baseMapper.showUserCollectionWord(page, user.getId()));
        return Result.success(ResultCode.SUCCESS, page);
    }
}
