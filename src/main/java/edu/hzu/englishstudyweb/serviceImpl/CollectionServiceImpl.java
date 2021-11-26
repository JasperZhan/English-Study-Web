package edu.hzu.englishstudyweb.serviceImpl;

import edu.hzu.englishstudyweb.model.Collection;
import edu.hzu.englishstudyweb.mapper.CollectionMapper;
import edu.hzu.englishstudyweb.service.CollectionService;
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
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper, Collection> implements CollectionService {

}
