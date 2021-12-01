package edu.hzu.englishstudyweb;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.hzu.englishstudyweb.model.User;
import edu.hzu.englishstudyweb.model.Word;
import edu.hzu.englishstudyweb.service.ReviewSetService;
import edu.hzu.englishstudyweb.util.Result;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Jasper Zhan
 * @date 2021年12月01日 8:25
 */
@SpringBootTest
public class ReviewSetTests {

    @Resource
    ReviewSetService reviewSetService;

    @Test
    void addWordTest() {

        User user = new User();
        user.setId(10);
        List<Word> lists = new ArrayList<>();

        for(int i = 0; i < 100; i++) {
            int a = (int)(Math.random() * 100 + 1);
            Word word = new Word();
            word.setId(a);
            lists.add(word);
        }

        Result result;
        for (Word word: lists
             ) {
            result = reviewSetService.addWord(user, word);
            System.out.println(result.getCode());
        }
    }

    @Test
    void getPageOfWordByUserTest() {
        User user = new User();
        user.setId(10);

        Result result;

        result = reviewSetService.getPageOfWordByUser(user, 2, 20);

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
    void getUserCurrentReviewWordTest() {
        User user = new User();
        user.setId(10);
        Result result;
        result = reviewSetService.getUserCurrentReviewWord(user);

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
}
