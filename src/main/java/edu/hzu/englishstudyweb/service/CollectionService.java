package edu.hzu.englishstudyweb.service;

import edu.hzu.englishstudyweb.model.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.hzu.englishstudyweb.model.User;
import edu.hzu.englishstudyweb.util.Result;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jasper Zhan
 * @since 2021-11-26
 */
public interface CollectionService extends IService<Collection> {

    /**
     * 添加单词收藏
     * @author Jasper Zhan
     * @date 2021/11/27 12:14
     * @param collection 收藏实体类
     * @return edu.hzu.englishstudyweb.util.Result
     */
    Result addWord(Collection collection);

    /**
     * 删除单词收藏
     * @author Jasper Zhan
     * @date 2021/11/27 12:14
     * @param collection 收藏实体类
     * @return edu.hzu.englishstudyweb.util.Result
     */
    Result deleteWord(Collection collection);

    /**
     * 收藏的单词是否已存在
     * @author Jasper Zhan
     * @date 2021/11/27 12:31
     * @param collection 收藏实体类
     * @return edu.hzu.englishstudyweb.util.Result
     */
    Result isWordExist(Collection collection);

    /**
     *
     * @author Jasper Zhan
     * @date 2021/11/27 17:00
     * @param current 当前查询分页索引
     * @param number 查询结果条数
     * @param user 用户类
     * @return edu.hzu.englishstudyweb.util.Result>
     */
    Result showCollectionPage(int current, int number, User user);
}
