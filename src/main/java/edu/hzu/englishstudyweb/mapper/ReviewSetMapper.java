package edu.hzu.englishstudyweb.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.hzu.englishstudyweb.model.ReviewSet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.hzu.englishstudyweb.model.Word;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


import java.time.LocalDate;
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
public interface ReviewSetMapper extends BaseMapper<ReviewSet> {


    IPage<Word> showUserReviewWordPage(Page<?> page, @Param("userId") Integer userId);

    @Select("SELECT * " +
            "FROM sys_word a " +
            "RIGHT JOIN sys_review_set b on a.id = b.word_id " +
            "WHERE b.user_id = #{userId} " +
            "AND b.date_interval = DATEDIFF(#{currentDate}, b.study_date) ")
    List<Word> getUserCurrentReviewWord(@Param("userId") Integer userId, @Param("currentDate") LocalDate currentDate);
}
