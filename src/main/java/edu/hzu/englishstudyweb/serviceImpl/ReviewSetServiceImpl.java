package edu.hzu.englishstudyweb.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.hzu.englishstudyweb.model.ReviewSet;
import edu.hzu.englishstudyweb.mapper.ReviewSetMapper;
import edu.hzu.englishstudyweb.model.User;
import edu.hzu.englishstudyweb.model.Word;
import edu.hzu.englishstudyweb.service.ReviewSetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hzu.englishstudyweb.util.Result;
import edu.hzu.englishstudyweb.util.ResultCode;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jasper Zhan
 * @since 2021-11-26
 */
@Service
public class ReviewSetServiceImpl extends ServiceImpl<ReviewSetMapper, ReviewSet> implements ReviewSetService {

    /**
     * 添加单词到复习单词集
     * @param user 添加该单词的用户
     * @param word 添加的单词
     * @return edu.hzu.englishstudyweb.util.Result
     * @author Jasper Zhan
     * @date 2021/11/30 19:29
     */
    @Override
    public Result addWord(User user, Word word) {
        // 新建一个复习单词记录的实体
        ReviewSet reviewSet = new ReviewSet();
        try {
            reviewSet.setUserId(user.getId());
            reviewSet.setWordId(word.getId());
            reviewSet.setStudyDate(LocalDate.now());
        } catch (NullPointerException e) {
            return Result.failure(ResultCode.FAILURE_NULL_POINTER);
        }

        QueryWrapper<ReviewSet> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", reviewSet.getUserId())
                    .eq("word_id", reviewSet.getWordId());

        ReviewSet queryReviewSet;
        try {
            queryReviewSet = getOne(queryWrapper);
        } catch (Exception e) {
            return Result.failure(ResultCode.FAILURE_DATABASE);
        }

        if (queryReviewSet != null) {
            return Result.failure(ResultCode.FAILURE);
        }

        try {
            save(reviewSet);
        } catch (Exception e) {
            return Result.failure(ResultCode.FAILURE_DATABASE);
        }
        return Result.success(ResultCode.SUCCESS);
    }

    /**
     * 分页查询用户的复习单词
     * @param user    查询的用户
     * @param current 当前查询的分页索引
     * @param size 单词查询分页记录条数
     * @return edu.hzu.englishstudyweb.util.Result
     * @author Jasper Zhan
     * @date 2021/11/30 20:20
     */
    @Override
    public Result getPageOfWordByUser(User user, int current, int size) {
        if (user.getId() == null) {
            return Result.failure(ResultCode.FAILURE_NULL_POINTER);
        }

        Page<Word> page = new Page<>(current, size);
        IPage<Word> queryWordPage;

        try {
            queryWordPage = this.baseMapper.showUserReviewWordPage(page, user.getId());
        } catch (Exception e) {
            throw e;
            //return Result.failure(ResultCode.FAILURE_DATABASE);
        }

        return Result.success(ResultCode.SUCCESS, queryWordPage.getRecords());
    }

    /**
     * 查询用户当前需复习的全部单词
     * @param user 查询的用户
     * @return edu.hzu.englishstudyweb.util.Result
     * @author Jasper Zhan
     * @date 2021/11/30 20:43
     */
    @Override
    public Result getUserCurrentReviewWord(User user) {
        if (user.getId() == null) {
            return Result.failure(ResultCode.FAILURE_NULL_POINTER);
        }

        List<Word> queryWords;
        try {
            queryWords = this.baseMapper.getUserCurrentReviewWord(user.getId(), LocalDate.now());
        } catch (Exception e) {
            throw e;
            //return Result.failure(ResultCode.FAILURE_DATABASE);
        }

        return Result.success(queryWords);
    }

    /**
     * 更新用户当前复习单词的状态
     * @param reviewSets 更新的复习单词集
     * @return edu.hzu.englishstudyweb.util.Result
     * @author Jasper Zhan
     * @date 2021/12/1 8:09
     */
    @Override
    public Result updateReviewWordStatus(List<ReviewSet> reviewSets) {

        if (reviewSets == null) {
            return Result.failure(ResultCode.FAILURE_NULL_POINTER);
        }

        if (updateBatchById(reviewSets)) {
            return Result.success();
        }
        return Result.failure(ResultCode.FAILURE);
    }
}
