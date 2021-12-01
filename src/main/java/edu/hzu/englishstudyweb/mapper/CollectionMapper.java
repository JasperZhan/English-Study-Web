package edu.hzu.englishstudyweb.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.hzu.englishstudyweb.model.Collection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.hzu.englishstudyweb.model.Word;
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
public interface CollectionMapper extends BaseMapper<Collection> {

    @Select("select * from sys_word a " +
            "RIGHT JOIN sys_collection b on a.id = b.word_id " +
            "WHERE b.user_id = #{userId}")
    List<Word> showUserCollectionWord(@Param("page") Page<Word> page, @Param("userId") Integer userId);
}
