package edu.hzu.englishstudyweb.controller;


import cn.dev33.satoken.stp.StpUtil;
import edu.hzu.englishstudyweb.model.Collection;
import edu.hzu.englishstudyweb.model.User;
import edu.hzu.englishstudyweb.model.Word;
import edu.hzu.englishstudyweb.service.CollectionService;
import edu.hzu.englishstudyweb.util.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Jasper Zhan
 * @since 2021-11-26
 */
@Controller
public class CollectionController {

    @Resource
    private CollectionService collectionService;

    @RequestMapping("/collection")
    public String collection(Model model, @RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
                             @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        if (!StpUtil.isLogin()) {
            return "redirect:login";
        }
        User user = new User();
        user.setId(StpUtil.getLoginIdAsInt());
        Result result = collectionService.showCollectionPage(pageIndex, pageSize, user);
        if (!result.isSuccess()) {
            return result.getMsg();
        }
        List<Word> wordList = (List<Word>) result.getData();
        result = collectionService.getCollectionNum(StpUtil.getLoginIdAsInt());
        if (!result.isSuccess()) {
            return result.getMsg();
        }
        long count = (long)result.getData();
        System.out.println(count);
        model.addAttribute("wordList", wordList);
        model.addAttribute("pageNum", count % 10 == 0 ? count/10 : count /10 + 1);
        model.addAttribute("currentPage", pageIndex);
        return "collection";
    }

    @ResponseBody
    @RequestMapping("/collection/add/")
    public String add(String wordId) {
//        if (!StpUtil.isLogin()) {
//            return "当前未登录";
//        }
        System.out.println(wordId);
        Collection collection = new Collection();
        collection.setUserId(StpUtil.getLoginIdAsInt());
        collection.setWordId(Integer.valueOf(wordId));
        Result result = collectionService.addWord(collection);
        if (result.isSuccess()) {
            return "已收藏";
        } else {
            return "收藏";
        }

    }

    @RequestMapping("/collection/delete")
    @ResponseBody
    public String delete(String collection_id) {
        if (!StpUtil.isLogin()) {
            return "当前未登录";
        }
        Collection collection = new Collection();
        collection.setId(Integer.valueOf(collection_id));
        Result result = collectionService.deleteWord(collection);
        if (result.isSuccess()) {
            return "删除收藏成功";
        } else {
            return "删除收藏失败";
        }
    }

}
