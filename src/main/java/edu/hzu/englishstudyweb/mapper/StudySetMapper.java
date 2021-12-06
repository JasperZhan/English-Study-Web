package edu.hzu.englishstudyweb.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.hzu.englishstudyweb.model.StudySet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.hzu.englishstudyweb.model.Word;
import edu.hzu.englishstudyweb.util.Result;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

    Integer insertWord(Integer userId);

    List<StudySet> selectWord(Integer user_id, Integer word_status);

    Integer deleteSetWord(Integer id);

    StudySet selectWordByWid(Integer word_id);

    IPage<Word> showUserStudyWordPage(Page<?> page, @Param("userId") Integer userId);

    IPage<StudySet> showUserStudySetPage(Page<?> page, @Param("userId") Integer userId);
}
