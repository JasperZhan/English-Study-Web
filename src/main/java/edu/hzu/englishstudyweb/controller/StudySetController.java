package edu.hzu.englishstudyweb.controller;


import cn.dev33.satoken.stp.StpUtil;
import edu.hzu.englishstudyweb.model.StudySet;
import edu.hzu.englishstudyweb.model.User;
import edu.hzu.englishstudyweb.model.Word;
import edu.hzu.englishstudyweb.service.ReviewSetService;
import edu.hzu.englishstudyweb.service.StudySetService;
import edu.hzu.englishstudyweb.util.RedisUtil;
import edu.hzu.englishstudyweb.util.Result;
import edu.hzu.englishstudyweb.util.SerializeUtil;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Jasper Zhan
 * @since 2021-11-26
 */
@RestController
@RequestMapping("/englishstudyweb/study-set")
public class StudySetController {


    @Resource
    StudySetService studySetService;

    @Resource
    ReviewSetService reviewSetService;

    public Jedis jedis = RedisUtil.getJedis();

    public boolean judge = false;

    public  List<Integer> totalList = new LinkedList<>(); // 用来保存背诵过程中剩余的单词id 和 显示剩余单词数量
    public  Queue<Integer> knowQueue = new LinkedList<>();
    public  Queue<Integer> vagueQueue = new LinkedList<>();
    public  Queue<Integer> forgetQueue = new LinkedList<>();




    @RequestMapping("add")
    public String add(int wordNum,String wordLevel) {
        if (!StpUtil.isLogin()) {
            return "当前未登录";
        }
        StudySet studySet = new StudySet();
        studySet.setUser_id(StpUtil.getLoginIdAsInt());
        studySet.setWord_status(1);
        studySet.setWord_count(0);
        Result result = studySetService.addWord(studySet,wordNum,wordLevel);
        if (result.isSuccess()) {
            return "单词成功加入学习集";
        } else {
            return "单词加入学习集失败";
        }
    }


    @RequestMapping("/study")
    public String study(Model model) {  // 点击开始学习后先从学习集里面获取 单词 加入到初始队列里面
        if (!StpUtil.isLogin()) {
            return "当前未登录";
        }
        Result result = studySetService.selectWord(StpUtil.getLoginIdAsInt(),1); //选取单词状态是1的单词
        List<Integer> word_id= (List<Integer>) result.getData(); // 从学习集里面获取 即将要学习的单词 id 列表
        System.out.println(word_id);
        for (Integer integer : word_id) {
            totalList.add(integer);  //将单词id列表加入到List里面
            forgetQueue.offer(integer);  // 将单词加入忘记队列
            StudySet studySet = (StudySet) studySetService.selectWordByWid(integer).getData(); // 从学习集里面找出单词
            jedis.set(integer.toString().getBytes(),SerializeUtil.serialize(studySet)); // 将单词id 作为key, 整个单词作为value 存入redis
        }
        result = studySetService.selectWordByWid(forgetQueue.peek()); // 获取第一个单词 但先不出队 后面统一出队
        model.addAttribute("studySet",result.getData());  // 页面展示的第一个单词
        model.addAttribute("lastWordNum",totalList.size()); // 显示剩余需要背诵的单词数量
         return "study";
    }




    @PostMapping("know")
    @ResponseBody
    public String know(Model model,@RequestBody(required = true) StudySet studySet) { // 点击了认识按钮 修改对应状态  存入redis
        if (!StpUtil.isLogin()) {
            return "当前未登录";
        }
        studySet.setWord_count(studySet.getWord_count()+1); // 单词背诵了一次 count += 1

        if (studySet.getWord_status().equals(3)) {  // 单词点击认识前的status是认识 : 出队
             knowQueue.poll(); // 认识队列队头出队
            totalList.remove(studySet.getWord_id());  //将背会的单词从总背诵的列表删除
            if (studySet.getWord_count()>5) {  // 如果该单词总背诵超过5次，需要加入复习集里面
                // 加入复习集合
                User user = new User();
                Word word = new Word();
                word.setId(studySet.getWord_id());
                user.setId(StpUtil.getLoginIdAsInt());
                reviewSetService.addWord(user,word);
            }
        } else if(studySet.getWord_status().equals(2)) { // 单词前一个状态是：模糊
            studySet.setWord_status(3);  // 单词状态变为认识
            knowQueue.offer(vagueQueue.poll());  // 模糊队列队头 出队 并且 加入认识队列



        }else if(studySet.getWord_status().equals(1)) { // 单词前一个状态是：不认识
            studySet.setWord_status(3);  // 单词状态变为认识
            knowQueue.offer(forgetQueue.poll());  // 不认识队列队头出队 并且 加入认识队列
        }

        jedis.set(studySet.getWord_id().toString().getBytes(),SerializeUtil.serialize(studySet));


        // 点击认识按钮 单词的状态修改后，即将从三个队列中，寻找下一个要背诵的单词
        if (totalList.isEmpty()) {   // 如果要背诵的单词全部背完了
           studySetService.deleteWord(StpUtil.getLoginIdAsInt());
            System.out.println("背完全部单词，并删除学习集里面的单词");

            return "背完全部单词";
        } else {  // 从三个队列中寻找下一个单词

             judge = false;  // 判断是否找到单词
            StudySet newWord = new StudySet(); // 下一个背诵的单词对象

                while (!forgetQueue.isEmpty()) {  // 先从不认识队列里面寻找要背的单词
                    // 从redis 中 依据wid 作为key  寻找单词
                    newWord =(StudySet) SerializeUtil.unSerialize(jedis.get(forgetQueue.peek().toString().getBytes()));
                    judge = true;
                    break;
                }

            if (!judge) {
                while (!vagueQueue.isEmpty()) { // 不认识队列没有单词 从模糊队列找
                    newWord =(StudySet) SerializeUtil.unSerialize(jedis.get(vagueQueue.peek().toString().getBytes()));
                    judge = true;
                    break;
                }
            }
            if (!judge) {
                while (!knowQueue.isEmpty()) {
                    newWord =(StudySet) SerializeUtil.unSerialize(jedis.get(knowQueue.peek().toString().getBytes()));
                    break;
                }
            }
            System.out.println("下一个要学习的单词:"+newWord);
            model.addAttribute("newWord",newWord);  // 下一个要背诵的单词
            model.addAttribute("lastWordNum",totalList.size()); // 剩余单词量
            return "学习的页面";
        }
    }

    @PostMapping("vague")
    @ResponseBody
    public String vague(Model model,@RequestBody(required = true) StudySet studySet) {
        if (!StpUtil.isLogin()) {
            return "当前未登录";
        }
        studySet.setWord_count(studySet.getWord_count()+2); // 点击了模糊 背诵的次数 + 2

        if (studySet.getWord_status().equals(3)) {  // 单词点击认识前的status 是认识
            studySet.setWord_status(2);
            vagueQueue.offer(knowQueue.poll());  // 从认识队列里面 入队到模糊队列
        } else if(studySet.getWord_status().equals(2)) { // 单词前一个状态是：模糊
            vagueQueue.offer(vagueQueue.poll());  // 模糊队列队头插入队尾
        }else if(studySet.getWord_status().equals(1)) { // 单词前一个状态是：不认识
            studySet.setWord_status(2);  // 单词状态变为模糊
            vagueQueue.offer(forgetQueue.poll());  // 不认识队列队头出队 并且 加入模糊队列
        }

        // 点击模糊按钮后在redis中修改对应单词状态
        jedis.set(studySet.getWord_id().toString().getBytes(),SerializeUtil.serialize(studySet));


        judge = false;  // 判断是否找到单词
        StudySet newWord = new StudySet(); // 下一个背诵的单词对象

        while (!forgetQueue.isEmpty()) {  // 先从不认识队列里面寻找要背的单词
            // 从redis 中 依据wid 作为key  寻找单词
            newWord =(StudySet) SerializeUtil.unSerialize(jedis.get(forgetQueue.peek().toString().getBytes()));
            judge = true;
            break;
        }

        if (!judge) {
            while (!vagueQueue.isEmpty()) { // 不认识队列没有单词 从模糊队列找
                newWord =(StudySet) SerializeUtil.unSerialize(jedis.get(vagueQueue.peek().toString().getBytes()));
                judge = true;
                break;
            }
        }
        if (!judge) {
            while (!knowQueue.isEmpty()) {
                newWord =(StudySet) SerializeUtil.unSerialize(jedis.get(knowQueue.peek().toString().getBytes()));
                break;
            }
        }
        System.out.println("下一个要学习的单词:"+newWord);

        model.addAttribute("newWord",newWord);  // 下一个要背诵的单词
        model.addAttribute("lastWordNum",totalList.size()); // 剩余单词量
        return "学习的页面";

    }

    @PostMapping("forget")
    @ResponseBody
    public String forget(Model model, @RequestBody(required = true)StudySet studySet) {
        if (!StpUtil.isLogin()) {
            return "当前未登录";
        }
        studySet.setWord_count(studySet.getWord_count()+3);  // 点击了不认识 背诵次数 + 3

        if (studySet.getWord_status().equals(3)) {  // 单词前一个状态是认识
            studySet.setWord_status(1);
            forgetQueue.offer(knowQueue.poll());  // 从认识队列里面 入队到忘记队列
        } else if(studySet.getWord_status().equals(2)) { // 单词前一个状态是：模糊
            studySet.setWord_status(1);
            forgetQueue.offer(vagueQueue.poll());  // 模糊队列队头出队插入忘记队列
        }else if(studySet.getWord_status().equals(1)) { // 单词前一个状态是：不认识
            forgetQueue.offer(forgetQueue.poll());  // 忘记队列队头插入队尾
        }

        // 点击忘记按钮后在redis中修改对应单词状态
        jedis.set(studySet.getWord_id().toString().getBytes(),SerializeUtil.serialize(studySet));


       judge = false;  // 判断是否找到单词
        StudySet newWord = new StudySet(); // 下一个背诵的单词对象

        while (!forgetQueue.isEmpty()) {  // 先从不认识队列里面寻找要背的单词
            // 从redis 中 依据wid 作为key  寻找单词
            newWord =(StudySet) SerializeUtil.unSerialize(jedis.get(forgetQueue.peek().toString().getBytes()));
            judge = true;
            break;
        }

        if (!judge) {
            while (!vagueQueue.isEmpty()) { // 不认识队列没有单词 从模糊队列找
                newWord =(StudySet) SerializeUtil.unSerialize(jedis.get(vagueQueue.peek().toString().getBytes()));
                judge = true;
                break;
            }
        }
        if (!judge) {
            while (!knowQueue.isEmpty()) {
                newWord =(StudySet) SerializeUtil.unSerialize(jedis.get(knowQueue.peek().toString().getBytes()));
                break;
            }
        }
        System.out.println("下一个要学习的单词:"+newWord);

        model.addAttribute("newWord",newWord);  // 下一个要背诵的单词
        model.addAttribute("lastWordNum",totalList.size()); // 剩余单词量
        return "学习的页面";

    }

}
