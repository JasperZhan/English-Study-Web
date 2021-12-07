package edu.hzu.englishstudyweb.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import edu.hzu.englishstudyweb.model.*;
import edu.hzu.englishstudyweb.service.ReviewSetService;
import edu.hzu.englishstudyweb.service.WordService;
import edu.hzu.englishstudyweb.util.RedisUtil;
import edu.hzu.englishstudyweb.util.Result;
import edu.hzu.englishstudyweb.util.SerializeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Jasper Zhan
 * @since 2021-11-26
 */
@Controller
public class ReviewSetController {

    @Resource
    ReviewSetService reviewSetService;

    @Resource
    WordService wordService;

    private static Integer currentReviewId = null;

    public Jedis jedis = RedisUtil.getJedis();

    public Map<Integer, ReviewSet> reviewSetMap = new HashMap<>(); // 用来保存背诵过程中剩余的单词id 和 显示剩余单词数量
    public Queue<Integer> knowQueue = new LinkedList<>();
    public Queue<Integer> vagueQueue = new LinkedList<>();
    public Queue<Integer> forgetQueue = new LinkedList<>();



    @RequestMapping("/review")
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

        User user = new User();
        user.setId(StpUtil.getLoginIdAsInt());
        Result result = reviewSetService.getUserCurrentReviewWord(user);

        if (!result.isSuccess()) {
            System.out.println(result.getMsg());
        }

        List<ReviewSet> reviewSets = (List<ReviewSet>) result.getData();

        // 学习单词初始化，都是忘记状态
        for (ReviewSet reviewSet: reviewSets) {
            System.out.println(reviewSet);
            reviewSetMap.put(reviewSet.getId(), reviewSet);
            forgetQueue.offer(reviewSet.getId());
        }

        WordResult wordResult = returnDate();

        model.addAttribute("word_english", wordResult.getWord_english());  // 页面展示的第一个单词
        model.addAttribute("word_chinese", wordResult.getWord_chinese());  // 页面展示的第一个单词
        model.addAttribute("word_total", wordResult.getWord_total()); // 显示剩余需要背诵的单词数量
        return "review";
    }


    @PostMapping("/review/know")
    @ResponseBody
    @SaCheckLogin
    public WordResult know() {

        ReviewSet reviewSet = reviewSetMap.get(currentReviewId);
        int knowCount = reviewSet.getKnowCount() + 1;
        reviewSet.setKnowCount(knowCount);

        // 如果用户第一次复习该单词就已经认识，直接更新到下一次复习
        if (reviewSet.getCurrentStatus().equals(0)) {
            forgetQueue.poll();
            reviewSetMap.put(reviewSet.getId(), reviewSet);
            updateReview(reviewSet);
        }

        // 单词点击认识前的status是认识
        if (reviewSet.getCurrentStatus().equals(3)) {
            knowQueue.poll(); // 认识队列队头出队
            reviewSetMap.put(reviewSet.getId(), reviewSet);
            updateReview(reviewSet);
        }
        // 单词前一个状态是：模糊
        else if (reviewSet.getCurrentStatus().equals(2)) {
            knowQueue.offer(vagueQueue.poll());
            reviewSet.setCurrentStatus(3);
            reviewSetMap.put(reviewSet.getId(), reviewSet);
        }
        // 单词前一个状态是：不认识
        else if (reviewSet.getCurrentStatus().equals(1)) {
            vagueQueue.offer(forgetQueue.poll());
            reviewSet.setCurrentStatus(2);
            reviewSetMap.put(reviewSet.getId(), reviewSet);
        }

        return returnDate();
    }

    @PostMapping("review/vague")
    @ResponseBody
    @SaCheckLogin
    public WordResult vague() {

        ReviewSet reviewSet = reviewSetMap.get(currentReviewId);
        int vagueCount = reviewSet.getVagueCount() + 1;
        reviewSet.setVagueCount(vagueCount);

        // 单词点击认识前的status是认识
        if (reviewSet.getCurrentStatus().equals(3)) {
            vagueQueue.offer(knowQueue.poll()); // 认识队列队头出队
            reviewSet.setCurrentStatus(2);
            reviewSetMap.put(reviewSet.getId(), reviewSet);
        }
        // 单词前一个状态是：模糊
        else if (reviewSet.getCurrentStatus().equals(2)) {
            // 模糊队列队头插入队尾
            vagueQueue.offer(vagueQueue.poll());
            reviewSetMap.put(reviewSet.getId(), reviewSet);
        }
        // 单词前一个状态是：不认识
        else if (reviewSet.getCurrentStatus().equals(1)) {
            vagueQueue.offer(forgetQueue.poll());
            reviewSet.setCurrentStatus(2);
            reviewSetMap.put(reviewSet.getId(), reviewSet);
        }

        return returnDate();
    }

    @PostMapping("review/forget")
    @ResponseBody
    @SaCheckLogin
    public WordResult forget() {

        ReviewSet reviewSet = reviewSetMap.get(currentReviewId);
        int forgetCount = reviewSet.getForgetCount() + 1;
        reviewSet.setForgetCount(forgetCount);

        // 单词点击认识前的status是认识
        if (reviewSet.getCurrentStatus().equals(3)) {
            forgetQueue.offer(knowQueue.poll()); // 认识队列队头出队
            reviewSet.setCurrentStatus(1);
            reviewSetMap.put(reviewSet.getId(), reviewSet);
        }
        // 单词前一个状态是：模糊
        else if (reviewSet.getCurrentStatus().equals(2)) {
            forgetQueue.offer(vagueQueue.poll());
            reviewSet.setCurrentStatus(1);
            reviewSetMap.put(reviewSet.getId(), reviewSet);
        }
        // 单词前一个状态是：不认识
        else if (reviewSet.getCurrentStatus().equals(1)) {
            // 忘记队列队头插入队尾
            forgetQueue.offer(forgetQueue.poll());
            reviewSetMap.put(reviewSet.getId(), reviewSet);
        }

        return returnDate();
    }

    private void updateReview(ReviewSet reviewSet) {

        System.out.println("11111111111111111111");

        reviewSet.setCurrentStatus(0);

        int dateInterval = reviewSet.getDateInterval();
        int knowCount = reviewSet.getKnowCount();
        int vagueCount = reviewSet.getVagueCount();
        int forgetCount = reviewSet.getForgetCount();
        reviewSet.setDateInterval(dateInterval * 2 - (int)(dateInterval * (knowCount * 0.1 + vagueCount * 0.3 + forgetCount * 0.6)));

        reviewSetService.updateReviewWordStatus(reviewSet);
        reviewSetMap.remove(reviewSet.getId());

        System.out.println(reviewSet);
    }

    private WordResult returnDate() {

        if (!forgetQueue.isEmpty()) {
            currentReviewId = forgetQueue.peek();
        } else if (!vagueQueue.isEmpty()) {
            currentReviewId = vagueQueue.peek();
        } else if (!knowQueue.isEmpty()) {
            currentReviewId = knowQueue.peek();
        }

        ReviewSet reviewSet = reviewSetMap.get(currentReviewId);
        Word word = (Word) SerializeUtil.unSerialize(jedis.get(reviewSet.getWordId().toString().getBytes()));

        assert word != null;
        System.out.println(reviewSet);
        System.out.println("下一个要学习的单词:" + word.getId() + "study_set id:" + currentReviewId);

        WordResult wordResult = new WordResult();
        wordResult.setWord_id(word.getId());
        wordResult.setWord_status(reviewSet.getCurrentStatus());
        wordResult.setWord_total(reviewSetMap.size());
        wordResult.setWord_chinese(word.getChinese());
        wordResult.setWord_english(word.getEnglish());
        wordResult.setWord_level(word.getLevel());

        return wordResult;
    }
}
