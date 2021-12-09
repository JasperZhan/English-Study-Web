package edu.hzu.englishstudyweb.service;

import edu.hzu.englishstudyweb.config.MybatisPlusConfig;
import edu.hzu.englishstudyweb.model.StudySet;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.hzu.englishstudyweb.model.User;
import edu.hzu.englishstudyweb.util.Result;
import io.swagger.models.auth.In;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jasper Zhan
 * @since 2021-11-26
 */
public interface StudySetService extends IService<StudySet> {


//     Result addWord(StudySet studySet,int wordNum,String wordLevel);
     Result addWord(User user);

     Result getPageOfWordByUser(User user, int current, int size);

     Result selectWord(Integer user_id,Integer word_status);

     Result selectWordByWid(Integer word_id);

     Result deleteWord(Integer id);

     Result getPageOfStudyByUser(Integer user_id, int current, int size);

     Result deleteAllWordByUser(Integer userId);

}
