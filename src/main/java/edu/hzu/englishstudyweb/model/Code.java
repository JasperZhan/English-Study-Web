package edu.hzu.englishstudyweb.model;

import lombok.Data;

/**
 * @author Jasper Zhan
 * @date 2021年12月09日 20:44
 */
@Data
public class Code {
    /**
     * 验证码
     */
    private String code;

    /**
     * 当前时间
     */
    private Long currentTime;
}
