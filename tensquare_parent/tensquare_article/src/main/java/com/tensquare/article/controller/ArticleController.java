package com.tensquare.article.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.tensquare.article.pojo.Article;
import com.tensquare.article.service.ArticleService;
import com.tensquare.entity.PageResult;
import com.tensquare.entity.Result;
import com.tensquare.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/article")
@CrossOrigin
public class ArticleController {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ArticleService articleService;

    //    POST/article 增加文章
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Article article) {

        articleService.save(article);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    //    DELETE/article/{articleId}根据ID删除文章
    @RequestMapping(value = "/{articleId}", method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String articleId) {

        articleService.deleteById(articleId);
        return new Result(true, StatusCode.OK, "删除成功");
    }

//    PUT/article/{articleId}修改文章

    @RequestMapping(value = "/{articleId}", method = RequestMethod.PUT)
    public Result updateById(@PathVariable String articleId, @RequestBody Article article) {
        article.setId(articleId);
        articleService.updateById(article);
        return new Result(true, StatusCode.OK, "修改成功");
    }

//    分页查询
//    POST/article/search/{page}/{size}文章分页

    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findByPage(@PathVariable Integer page, @PathVariable Integer size, @RequestBody Map<String, Object> map) {
//        获取分页数据
        Page<Article> pageData = articleService.findByPage(page, size, map);
//        封装分页结果
        PageResult<Article> pageResult = new PageResult<>(pageData.getTotal(), pageData.getRecords());
//        返回分页数据
        return new Result(true, StatusCode.OK, "查询成功");
    }

    // GET /article/{articleId} 根据ID查询文章
    @RequestMapping(value = "{articleId}", method = RequestMethod.GET)
    public Result findById(@PathVariable String articleId) {
        Article article = articleService.findById(articleId);
        return new Result(true, StatusCode.OK, "查询成功", article);
    }

    //    根据文章Id订阅或者取消订阅作者
    @RequestMapping(value = "/subscribe", method = RequestMethod.POST)
//用户根据文章id与文章作者发生联系，前台传入文章id和用户id，可以用map来接收
    public Result subscribe(@RequestBody Map map) {
        Boolean flag = articleService.subscribe(map.get("userId").toString(), map.get("articleId").toString());
        if (flag) {
            return new Result(true, StatusCode.OK, "订阅成功");
        } else {
            return new Result(false, StatusCode.OK, "取消订阅成功");
        }

    }

    //    文章点赞
    @RequestMapping(value = "thumbup/{articleId}", method = RequestMethod.PUT)
    public Result thumbup(@PathVariable String articleId) {
//        增加判断redis值来防止重复点赞
//        增加用户
        String userId = "4";
        Object result = redisTemplate.boundValueOps("thumbup_article_" + userId + "_" + articleId).get();
        if (result != null) {
            return new Result(false, StatusCode.REMOTEERROR, "不能重复点赞");
        } else {
            articleService.thumbup(articleId,userId);
            redisTemplate.opsForValue().set("thumbup_article_" + userId + "_" + articleId, 1);
            return new Result(true, StatusCode.OK, "点赞成功");
        }

    }
}
