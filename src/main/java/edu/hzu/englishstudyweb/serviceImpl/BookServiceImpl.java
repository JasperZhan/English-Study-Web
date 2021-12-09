package edu.hzu.englishstudyweb.serviceImpl;

import edu.hzu.englishstudyweb.model.Book;
import edu.hzu.englishstudyweb.mapper.BookMapper;
import edu.hzu.englishstudyweb.service.BookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hzu.englishstudyweb.util.Result;
import edu.hzu.englishstudyweb.util.ResultCode;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
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
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {
    @Override
    public Result getBook() {
        List<Book> books = list();
        if (books == null || books.size() == 0) {
            return Result.failure(ResultCode.FAILURE_OF_QUERY_NULL);
        }
        return Result.success(books);
    }

}
