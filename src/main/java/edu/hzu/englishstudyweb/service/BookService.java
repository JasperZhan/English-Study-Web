package edu.hzu.englishstudyweb.service;

import edu.hzu.englishstudyweb.model.Book;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.hzu.englishstudyweb.util.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jasper Zhan
 * @since 2021-11-26
 */
public interface BookService extends IService<Book> {

    Result getBook();

}
