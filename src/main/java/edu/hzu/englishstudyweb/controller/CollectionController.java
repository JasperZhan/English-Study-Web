package edu.hzu.englishstudyweb.controller;


import cn.dev33.satoken.stp.StpUtil;
import edu.hzu.englishstudyweb.model.Collection;
import edu.hzu.englishstudyweb.model.User;
import edu.hzu.englishstudyweb.model.Word;
import edu.hzu.englishstudyweb.service.CollectionService;
import edu.hzu.englishstudyweb.util.Result;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
@RestController
@RequestMapping("/collection/")
public class CollectionController {

    @Resource
    private CollectionService collectionService;

    public String collection() {
        if (StpUtil.isLogin()) {
            return "collection";
        } else {
            return "login";
        }
    }

    @RequestMapping("add")
    public String add(String wordId) {
        if (!StpUtil.isLogin()) {
            return "当前未登录";
        }
        Collection collection = new Collection();
        collection.setUserId(StpUtil.getLoginIdAsInt());
        collection.setWordId(Integer.valueOf(wordId));
        Result result = collectionService.addWord(collection);
        if (result.isSuccess()) {
            return "添加收藏成功";
        } else {
            return "添加收藏失败";
        }
    }

    @RequestMapping("delete")
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

    @RequestMapping("list")
    public String list(int pageIndex, Model model) {
        if (!StpUtil.isLogin()) {
            return "当前未登录";
        }
        User user = new User();
        user.setId(StpUtil.getLoginIdAsInt());
        //这里保留一个number参数，后期可以添加单分页展现多少单词条数
        Result result = collectionService.showCollectionPage(pageIndex, 20, user);
        if (!result.isSuccess()) {
            return "查询失败"+result.getMsg();
        }
        List<Word> wordList = (List<Word>) result.getData();
        model.addAttribute("wordList", wordList);
        return "collectionList";
    }
}
