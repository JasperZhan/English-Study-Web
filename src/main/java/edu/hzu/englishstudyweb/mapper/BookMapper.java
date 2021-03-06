package edu.hzu.englishstudyweb.mapper;

import edu.hzu.englishstudyweb.model.Book;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Jasper Zhan
 * @since 2021-11-26
 */
@Mapper
public interface BookMapper extends BaseMapper<Book> {

}
