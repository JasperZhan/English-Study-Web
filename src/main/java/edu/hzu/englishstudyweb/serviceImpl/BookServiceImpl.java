package edu.hzu.englishstudyweb.serviceImpl;

import edu.hzu.englishstudyweb.model.Book;
import edu.hzu.englishstudyweb.mapper.BookMapper;
import edu.hzu.englishstudyweb.service.BookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
