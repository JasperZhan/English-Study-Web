package edu.hzu.englishstudyweb.mapper;

import edu.hzu.englishstudyweb.model.StudySet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.hzu.englishstudyweb.util.Result;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Jasper Zhan
 * @since 2021-11-26
 */
@Mapper
public interface StudySetMapper extends BaseMapper<StudySet> {

    Integer insertWord(StudySet studySet);

    List<Integer> selectWord(Integer user_id, Integer word_status);

    Integer deleteSetWord(Integer user_id);

    StudySet selectWordByWid(Integer word_id);

}
