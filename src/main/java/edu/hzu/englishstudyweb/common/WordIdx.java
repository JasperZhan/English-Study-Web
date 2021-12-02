package edu.hzu.englishstudyweb.common;


import edu.hzu.englishstudyweb.service.WordService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

/**
 * 从数据库获取分割四六级单词ID号
 */
@Data
@Component
public class WordIdx {

    @Autowired
    WordService wordService;

    public  static  WordIdx wordIdx;

    @PostConstruct
    public void init() {
        wordIdx = this;
    }

    public static final String FOUR_LEVEL = "4";
    public static final String SIX_LEVEL = "6";


    public Integer MAX_FOUR_IDX() {  // 四级单词的最大 id
        return wordIdx.wordService.Max_LevelIdx(FOUR_LEVEL);
    }

    public Integer MAX_SIX_IDX() {  // 六级单词的最大 id
        return wordIdx.wordService.Max_LevelIdx(SIX_LEVEL);
    }

}
