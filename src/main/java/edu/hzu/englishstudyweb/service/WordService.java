package edu.hzu.englishstudyweb.service;

import edu.hzu.englishstudyweb.model.Word;
import com.baomidou.mybatisplus.extension.service.IService;
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
public interface WordService extends IService<Word> {

    Result selectWord(int uid,int wordNum, String wordLevel);

    Integer Max_LevelIdx(String level);

    Result getWords();

}
