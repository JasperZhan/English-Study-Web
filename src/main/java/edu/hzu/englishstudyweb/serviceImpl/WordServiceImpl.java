package edu.hzu.englishstudyweb.serviceImpl;

import edu.hzu.englishstudyweb.model.Word;
import edu.hzu.englishstudyweb.mapper.WordMapper;
import edu.hzu.englishstudyweb.service.WordService;
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
public class WordServiceImpl extends ServiceImpl<WordMapper, Word> implements WordService {

}
