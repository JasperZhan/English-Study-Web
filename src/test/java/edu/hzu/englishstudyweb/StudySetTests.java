package edu.hzu.englishstudyweb;


import edu.hzu.englishstudyweb.model.StudySet;
import edu.hzu.englishstudyweb.model.User;
import edu.hzu.englishstudyweb.model.Word;
import edu.hzu.englishstudyweb.service.StudySetService;
import edu.hzu.englishstudyweb.service.WordService;
import edu.hzu.englishstudyweb.util.RedisUtil;
import edu.hzu.englishstudyweb.util.Result;
import edu.hzu.englishstudyweb.util.SerializeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@SpringBootTest
public class StudySetTests {
    @Resource
    private RedisTemplate redisTemplate;


    @Resource
    WordService wordService;

    @Resource
    StudySetService studySetService;

    @Test
    void addWordTest() {
        User user = new User();
        user.setId(6);
        Result result = studySetService.addWord(user);
        if (result.isSuccess()) {
            System.out.println("单词成功加入学习集");
        } else {
            System.out.println("单词加入学习集失败");
        }
    }

    @Test
    void getWordTest() {
        Result result = studySetService.selectWord(6,1);
        List<StudySet> studySets = (List<StudySet>) result.getData();
        for (StudySet studySet: studySets) {
            System.out.println(studySet.getId() + " " + studySet.getWord_status());
        }

    }
    @Test
    void selectWordByWidTest() {
        Result result = studySetService.selectWordByWid(3877);
        System.out.println(result.getData());
    }

    @Test
    void deleteWordTest() {
        Result result = studySetService.deleteWord(2);
        System.out.println("测试数据："+result.getData());
    }

    @Test
    void jedisResortTest() {

        StudySet [] studySets = new StudySet[2];
        StudySet [] studySets1 = new StudySet[2];

        List<StudySet> studySetList = new LinkedList<>();

        StudySet temp = new StudySet();
        temp.setWord_id(4999);
        temp.setUser_id(230);
        temp.setWord_count(10);
        temp.setWord_status(3);
        studySets[0] = temp;

        StudySet temp1 = new StudySet();
        temp1.setWord_id(4997);
        temp1.setUser_id(229);
        temp1.setWord_count(9);
        temp1.setWord_status(2);
        studySets[1] = temp1;
        Jedis jedis= RedisUtil.getJedis();
        Integer idx = 12356;

        //jedis.set("studySet".getBytes(),SerializeUtil.serialize(studySets));
        jedis.set(idx.toString().getBytes(),SerializeUtil.serialize(studySets));

         studySets1=(StudySet[]) SerializeUtil.unSerialize(jedis.get(idx.toString().getBytes()));

        System.out.println("测据单词id： "+studySets1[0].getWord_id());
        System.out.println("测据用户id： "+studySets1[0].getUser_id());
        System.out.println("测据单词count： "+studySets1[0].getWord_count());
        System.out.println("测据单词状态： "+studySets1[0].getWord_status());

        System.out.println("测据单词id： "+studySets1[1].getWord_id());
        System.out.println("测据用户id： "+studySets1[1].getUser_id());
        System.out.println("测据单词count： "+studySets1[1].getWord_count());
        System.out.println("测据单词状态： "+studySets1[1].getWord_status());

    }


    @Test
    void getPageOfWordByUserTest() {
        User user = new User();
        user.setId(6);

        Result result;

        result = studySetService.getPageOfWordByUser(user, 2, 20);

        if (!result.isSuccess()) {
            System.out.println(result.getCode());
            return;
        }

        List<Word> wordList = (List<Word>) result.getData();

        for (Word word: wordList
        ) {
            System.out.println("单词id" + word.getId() +
                    "，单词英语" + word.getEnglish() +
                    "，单词中文" + word.getChinese() +
                    "，单词英标" + word.getSent() +
                    "，单词等级" + word.getLevel());
        }
    }

    @Test
    public void deleteAllWordByUserTest() {
        studySetService.deleteAllWordByUser(6);
    }
}
