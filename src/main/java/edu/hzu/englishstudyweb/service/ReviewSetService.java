package edu.hzu.englishstudyweb.service;

import edu.hzu.englishstudyweb.model.ReviewSet;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.hzu.englishstudyweb.model.User;
import edu.hzu.englishstudyweb.model.Word;
import edu.hzu.englishstudyweb.util.Result;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jasper Zhan
 * @since 2021-11-26
 */
public interface ReviewSetService extends IService<ReviewSet> {
    /**
     * 添加单词到复习单词集
     * @author Jasper Zhan
     * @date 2021/11/30 19:29
     * @param user 添加该单词的用户
     * @param word 添加的单词
     * @return edu.hzu.englishstudyweb.util.Result
     */
    Result addWord(User user, Word word);

    /**
     * 分页查询用户的复习单词
     * @author Jasper Zhan
     * @date 2021/11/30 20:20
     * @param user 查询的用户
     * @param current 当前查询的分页索引
     * @param size 单词查询分页记录条数
     * @return edu.hzu.englishstudyweb.util.Result
     */
    Result getPageOfWordByUser(User user, int current, int size);

    /**
     * 查询用户当前需复习的全部单词
     * @author Jasper Zhan
     * @date 2021/11/30 20:43
     * @param user 查询的用户
     * @return edu.hzu.englishstudyweb.util.Result
     */
    Result getUserCurrentReviewWord(User user);

    /**
     * 更新用户当前复习单词的状态
     * @author Jasper Zhan
     * @date 2021/12/1 8:09
     * @param reviewSets 更新的复习单词集
     * @return edu.hzu.englishstudyweb.util.Result
     */
    Result updateReviewWordStatus(List<ReviewSet> reviewSets);
}
