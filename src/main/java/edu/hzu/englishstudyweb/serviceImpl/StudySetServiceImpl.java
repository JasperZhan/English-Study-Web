package edu.hzu.englishstudyweb.serviceImpl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.hzu.englishstudyweb.model.StudySet;
import edu.hzu.englishstudyweb.mapper.StudySetMapper;
import edu.hzu.englishstudyweb.model.Word;
import edu.hzu.englishstudyweb.service.StudySetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hzu.englishstudyweb.service.WordService;
import edu.hzu.englishstudyweb.util.Result;
import edu.hzu.englishstudyweb.util.ResultCode;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jasper Zhan
 * @since 2021-11-26
 */
@Service
public class StudySetServiceImpl extends ServiceImpl<StudySetMapper, StudySet> implements StudySetService {


    @Autowired
    WordService wordService;

    @Autowired
    StudySetMapper studySetMapper;

     @Override
     public Result addWord(StudySet studySet,int wordNum,String wordLevel) {
         int count = 0;
         Result result = wordService.selectWord(studySet.getUser_id(),wordNum,wordLevel);
         List<Integer> wordNumList = (List<Integer>) result.getData(); // 获取随机产生的wordNum个单词id

         for (Integer integer : wordNumList) {
             studySet.setWord_id(integer);
             studySetMapper.insertWord(studySet);
             count++;
         }
        if(count==wordNum) {
            return Result.success(ResultCode.SUCCESS);
        }else {
            return Result.failure(ResultCode.FAILURE);
        }

     }

     @Override
     public Result selectWord(Integer user_id,Integer word_status) {
         List<Integer> word_id = studySetMapper.selectWord(user_id,word_status);
         return Result.success(word_id);
     }

     @Override
     public Result selectWordByWid(Integer word_id) {
         StudySet studySet = studySetMapper.selectWordByWid(word_id);
         return Result.success(studySet);
     }

    @Override
    public Result deleteWord(Integer user_id) {
        Integer ans = studySetMapper.deleteSetWord(user_id);
        return Result.success(ans);
    }







}
