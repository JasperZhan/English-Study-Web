package edu.hzu.englishstudyweb;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.hzu.englishstudyweb.model.Collection;
import edu.hzu.englishstudyweb.model.User;
import edu.hzu.englishstudyweb.model.Word;
import edu.hzu.englishstudyweb.service.CollectionService;
import edu.hzu.englishstudyweb.util.Result;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Jasper Zhan
 * @date 2021年11月27日 12:52
 */
@SpringBootTest
public class CollectionTests {

    @Resource
    CollectionService collectionService;

    Collection collection;

    @Test
    void addWordTest() {
        collection = new Collection();
        collection.setUserId(5);
        collection.setWordId(3);
        collectionService.addWord(collection);
    }

    @Test
    void deleteWordTest() {
        collection = new Collection();
        collection.setId(1);
        Result result = collectionService.deleteWord(collection);
        System.out.println(result.getCode());
    }

    @Test
    void selectWordListPageTest() {
        User user = new User();
        user.setId(5);
        Result result = collectionService.showCollectionPage(1, 20, user);
        if (!result.isSuccess()) {
            System.out.println(result.getMsg());
            return;
        }
        Page<Word> page = (Page<Word>) result.getData();
        List<Word> wordList = page.getRecords();
        for (Word word : wordList) {
            System.out.println("单词id" + word.getId() +
                    "，单词英语" + word.getEnglish() +
                    "，单词中文" + word.getChinese() +
                    "，单词英标" + word.getSent() +
                    "，单词等级" + word.getLevel());
        }
    }
}
