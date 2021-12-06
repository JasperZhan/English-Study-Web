package edu.hzu.englishstudyweb.model;

import lombok.Data;

/**
 * @author Jasper Zhan
 * @date 2021年12月06日 21:57
 */
@Data
public class WordResult {
    Integer word_id;
    Integer word_status;
    Integer word_total;
    String word_chinese;
    String word_english;
    String word_level;
}
