package edu.hzu.englishstudyweb.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.hzu.englishstudyweb.model.ReviewSet;
import edu.hzu.englishstudyweb.model.StudySet;
import edu.hzu.englishstudyweb.mapper.StudySetMapper;
import edu.hzu.englishstudyweb.model.User;
import edu.hzu.englishstudyweb.model.Word;
import edu.hzu.englishstudyweb.service.StudySetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hzu.englishstudyweb.service.WordService;
import edu.hzu.englishstudyweb.util.Result;
import edu.hzu.englishstudyweb.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class StudySetServiceImpl extends ServiceImpl<StudySetMapper, StudySet> implements StudySetService {


    @Autowired
    WordService wordService;

    @Autowired
    StudySetMapper studySetMapper;


    /**
     * 根据输入单词数量和等级添加单词到学习集
     * @param studySet  加入学习集的对象
     * @param wordNum  要背诵单词数量
     * @param wordLevel   要背诵的单词等级
     * @return edu.hzu.englishstudyweb.util.Result>
     * @author zxb
     */
     @Override
     public Result addWord(User user) {
         if (user == null || user.getId() == null) {
             return Result.failure(ResultCode.FAILURE_NULL_POINTER);
         }
         studySetMapper.insertWord(user.getId());
//         int count = 0;
//         Result result = wordService.selectWord(studySet.getUser_id(),wordNum,wordLevel);
//         List<Integer> wordNumList = (List<Integer>) result.getData(); // 获取随机产生的wordNum个单词id
//
//         for (Integer integer : wordNumList) {
//             studySet.setWord_id(integer);
//             studySetMapper.insertWord(studySet);
//             count++;
//         }
//        if(count==wordNum) {
//            return Result.success(ResultCode.SUCCESS);
//        }else {
//            return Result.failure(ResultCode.FAILURE);
//        }
        return Result.success();
     }

    /**
     *  从学习集选择要背诵的单词ID列表
     * @param user_id  加入学习集的对象
     * @param word_status  要背诵单词数量
     * @return edu.hzu.englishstudyweb.util.Result>
     * @author zxb
     */
     @Override
     public Result selectWord(Integer user_id,Integer word_status) {
         List<StudySet> studySets = studySetMapper.selectWord(user_id,word_status);
         return Result.success(studySets);
     }

    /**
     *  根据单词ID从学习集选择单词
     * @param word_id  加入学习集的对象
     * @return edu.hzu.englishstudyweb.util.Result>
     * @author zxb
     */
     @Override
     public Result selectWordByWid(Integer word_id) {
         StudySet studySet = studySetMapper.selectWordByWid(word_id);
         return Result.success(studySet);
     }

    /**
     *  背诵完单词后从单词集删除全部单词
     * @param user_id  加入学习集的对象
     * @return edu.hzu.englishstudyweb.util.Result>
     * @author zxb
     */
    @Override
    public Result deleteWord(Integer id) {
        Integer ans = studySetMapper.deleteSetWord(id);
        return Result.success(ans);
    }



    @Override
    public Result getPageOfWordByUser(User user, int current, int size) {
        if (user == null || user.getId() == null) {
            return Result.failure(ResultCode.FAILURE_NULL_POINTER);
        }

        Page<Word> page = new Page<>(current, size);
        IPage<Word> queryWordPage;

        try {
            queryWordPage = this.baseMapper.showUserStudyWordPage(page, user.getId());
        } catch (Exception e) {
            throw e;
            //return Result.failure(ResultCode.FAILURE_DATABASE);
        }

        return Result.success(ResultCode.SUCCESS, queryWordPage.getRecords());
    }

    @Override
    public Result getPageOfStudyByUser(Integer user_id, int current, int size) {
        if (user_id == null) {
            return Result.failure(ResultCode.FAILURE_NULL_POINTER);
        }

        Page<StudySet> page = new Page<>(current, size);
        IPage<StudySet> queryWordPage;

        try {
            queryWordPage = this.baseMapper.showUserStudySetPage(page, user_id);
        } catch (Exception e) {
            return Result.failure(ResultCode.FAILURE_DATABASE);
        }

        return Result.success(ResultCode.SUCCESS, queryWordPage.getRecords());
    }
}
