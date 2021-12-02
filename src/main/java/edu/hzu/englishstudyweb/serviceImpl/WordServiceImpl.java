package edu.hzu.englishstudyweb.serviceImpl;

import edu.hzu.englishstudyweb.common.WordIdx;
import edu.hzu.englishstudyweb.model.Word;
import edu.hzu.englishstudyweb.mapper.WordMapper;
import edu.hzu.englishstudyweb.service.WordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hzu.englishstudyweb.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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



    @Autowired
    WordMapper wordMapper;

    @Resource
    RedisTemplate redisTemplate;

    public WordIdx wordIdx = new WordIdx();

    @Override
    public Result selectWord(int uid,int wordNum, String level) {
        int count = 0;
        int temp = 0;
        List<Integer> WordId = new ArrayList<>();
        if(level.equals(WordIdx.FOUR_LEVEL)) {
            do {
                temp = count;
                Random random = new Random();
                Integer wid = random.nextInt(wordIdx.MAX_FOUR_IDX());  // 从四级单词中随机产生单词 id
                count+=redisTemplate.opsForSet().add(uid,wid); // 将用户id作为 Key 单词id 作为value 存入 如果重复 返回0 不重复返回1
                if(temp!=count) {  // 新产生的 id 不会重复
                    WordId.add(wid); // 将随机产生的单词id 加入
                }
            }while(count!=wordNum); // 产生wordNum个单词后退出

        }else {
            do {
                Random random = new Random();
                Integer wid = random.nextInt(wordIdx.MAX_SIX_IDX()-wordIdx.MAX_FOUR_IDX())+wordIdx.MAX_FOUR_IDX(); // 从六级单词中随机产生单词 id
                count += redisTemplate.opsForSet().add(uid,wid);
                if(temp!=count) {  // 新产生的 id 不会重复
                    WordId.add(wid); // 将随机产生的单词id 加入
                }
            }while(count!=wordNum); // 产生wordNum个单词后退出
        }

        return Result.success(WordId);
    }

    @Override
    public Integer Max_LevelIdx(String level) {
        assert wordMapper != null;
        return wordMapper.Max_LevelIdx(level);
    }


}
