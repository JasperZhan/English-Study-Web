package edu.hzu.englishstudyweb.service;

import edu.hzu.englishstudyweb.util.Result;

/**
 * @author Jasper Zhan
 * @date 2021年12月09日 20:31
 */
public interface SmsService {
    Result sendCode(String tell);
}
