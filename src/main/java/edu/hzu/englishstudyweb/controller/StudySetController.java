package edu.hzu.englishstudyweb.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import edu.hzu.englishstudyweb.model.StudySet;
import edu.hzu.englishstudyweb.model.User;
import edu.hzu.englishstudyweb.model.Word;
import edu.hzu.englishstudyweb.model.WordResult;
import edu.hzu.englishstudyweb.service.ReviewSetService;
import edu.hzu.englishstudyweb.service.StudySetService;
import edu.hzu.englishstudyweb.service.WordService;
import edu.hzu.englishstudyweb.util.RedisUtil;
import edu.hzu.englishstudyweb.util.Result;
import edu.hzu.englishstudyweb.util.SerializeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Jasper Zhan
 * @since 2021-11-26
 */
@Controller
public class StudySetController {


    @Resource
    StudySetService studySetService;

    @Resource
    ReviewSetService reviewSetService;

    @Resource
    WordService wordService;

    private static StudySet currentStudy = null;

    public Jedis jedis = RedisUtil.getJedis();

    public boolean judge = false;

    public List<Integer> totalList = new ArrayList<>(); // 用来保存背诵过程中剩余的单词id 和 显示剩余单词数量
    public Queue<StudySet> knowQueue = new LinkedList<>();
    public Queue<StudySet> vagueQueue = new LinkedList<>();
    public Queue<StudySet> forgetQueue = new LinkedList<>();

    @RequestMapping("/study")
    public String study(Model model) {  // 点击开始学习后先从学习集里面获取 单词 加入到初始队列里面
        if (!StpUtil.isLogin()) {
            return "redirect:login";
        }

        // 将所有单词存入redis
        List<Word> wordList = (List<Word>) wordService.getWords().getData();
        for (Word word : wordList
        ) {
            // 将单词id 作为key, 整个单词作为value 存入redis
            jedis.set(word.getId().toString().getBytes(), SerializeUtil.serialize(word));
        }

        Result result = studySetService.getPageOfStudyByUser(StpUtil.getLoginIdAsInt(), 0, 20);

        if (!result.isSuccess()) {
            System.out.println(result.getMsg());
        }

        List<StudySet> studySets = (List<StudySet>) result.getData();

        // 学习单词初始化，都是忘记状态
        for (StudySet studySet : studySets) {
            System.out.println(studySet);
            totalList.add(studySet.getWord_id());
            forgetQueue.offer(studySet);
        }

        WordResult wordResult = returnDate();

        model.addAttribute("word_english", wordResult.getWord_english());  // 页面展示的第一个单词
        model.addAttribute("word_chinese", wordResult.getWord_chinese());  // 页面展示的第一个单词
        model.addAttribute("word_total", wordResult.getWord_total()); // 显示剩余需要背诵的单词数量
        return "study";
    }


    @PostMapping("/study/know")
    @ResponseBody
    @SaCheckLogin
    public WordResult know() {

        StudySet studySet;

        // 如果用户第一次学习该单词就已经认识，直接加入复习
        if (currentStudy.getWord_count().equals(0)) {
            forgetQueue.poll();
            addReview();
        }

        if (currentStudy.getWord_status().equals(3)) {  // 单词点击认识前的status是认识 : 出队
            knowQueue.poll(); // 认识队列队头出队
            addReview();

        } else if (currentStudy.getWord_status().equals(2)) { // 单词前一个状态是：模糊
            studySet = vagueQueue.poll();
            assert studySet != null;
            studySet.setWord_count(studySet.getWord_count() + 1);
            studySet.setWord_status(3);  // 单词状态变为认识
            knowQueue.offer(studySet);  // 模糊队列队头 出队 并且 加入认识队列
        } else if (currentStudy.getWord_status().equals(1)) { // 单词前一个状态是：不认识
            studySet = forgetQueue.poll();
            assert studySet != null;
            studySet.setWord_count(studySet.getWord_count() + 1);
            studySet.setWord_status(2);// 单词状态变为模糊
            vagueQueue.offer(studySet);  // 不认识队列队头出队 并且 加入模糊队列
        }
        return returnDate();
    }

    @PostMapping("study/vague")
    @ResponseBody
    @SaCheckLogin
    public WordResult vague() {

        StudySet studySet;

        if (currentStudy.getWord_status().equals(3)) {  // 单词点击认识前的status 是认识
            studySet = knowQueue.poll();
            assert studySet != null;
            studySet.setWord_count(studySet.getWord_count() + 1);
            studySet.setWord_status(2);
            vagueQueue.offer(studySet);  // 从认识队列里面 入队到模糊队列
        } else if (currentStudy.getWord_status().equals(2)) { // 单词前一个状态是：模糊
            studySet = vagueQueue.poll();
            assert studySet != null;
            studySet.setWord_count(studySet.getWord_count() + 1);
            vagueQueue.offer(studySet);  // 模糊队列队头插入队尾
        } else if (currentStudy.getWord_status().equals(1)) { // 单词前一个状态是：不认识
            studySet = forgetQueue.poll();
            assert studySet != null;
            studySet.setWord_count(studySet.getWord_count() + 1);
            studySet.setWord_status(2);
            vagueQueue.offer(studySet);  // 不认识队列队头出队 并且 加入模糊队列
        }

        return returnDate();
    }

    @PostMapping("study/forget")
    @ResponseBody
    @SaCheckLogin
    public WordResult forget() {
        StudySet studySet;

        if (currentStudy.getWord_status().equals(3)) {  // 单词前一个状态是认识
            studySet = knowQueue.poll();
            assert studySet != null;
            studySet.setWord_count(studySet.getWord_count() + 1);
            studySet.setWord_status(1);
            forgetQueue.offer(studySet);  // 从认识队列里面 入队到忘记队列
        } else if (currentStudy.getWord_status().equals(2)) { // 单词前一个状态是：模糊
            studySet = vagueQueue.poll();
            assert studySet != null;
            studySet.setWord_count(studySet.getWord_count() + 1);
            studySet.setWord_status(1);
            forgetQueue.offer(studySet);  // 模糊队列队头出队插入忘记队列
        } else if (currentStudy.getWord_status().equals(1)) { // 单词前一个状态是：不认识
            studySet = forgetQueue.poll();
            assert studySet != null;
            studySet.setWord_count(studySet.getWord_count() + 1);
            studySet.setWord_status(1);
            forgetQueue.offer(studySet);  // 忘记队列队头插入队尾
        }

        return returnDate();
    }

    private void addReview() {
        System.out.println("11111111111111111111");
        User user = new User();
        Word word = new Word();
        user.setId(StpUtil.getLoginIdAsInt());
        word.setId(currentStudy.getWord_id());
        System.out.println(currentStudy.getWord_id());
        reviewSetService.addWord(user, word);
        studySetService.deleteWord(currentStudy.getId());
        totalList.remove(currentStudy.getWord_id());
    }

    private WordResult returnDate() {

        StudySet studySet = null;

        if (!forgetQueue.isEmpty()) {
            studySet = forgetQueue.peek();
        } else if (!vagueQueue.isEmpty()) {
            studySet = vagueQueue.peek();
        } else if (!knowQueue.isEmpty()) {
            studySet = knowQueue.peek();
        }

        if (studySet == null) {
            return null;
        }

        currentStudy = studySet;
        Word word = (Word) SerializeUtil.unSerialize(jedis.get(studySet.getWord_id().toString().getBytes()));

        assert word != null;
        System.out.println("下一个要学习的单词:" + word.getId() + "study_set id:" + currentStudy.getId());


        WordResult wordResult = new WordResult();
        wordResult.setWord_id(word.getId());
        wordResult.setWord_status(currentStudy.getWord_status());
        wordResult.setWord_total(totalList.size());
        wordResult.setWord_chinese(word.getChinese());
        wordResult.setWord_english(word.getEnglish());
        wordResult.setWord_level(word.getLevel());

        return wordResult;
    }
}

