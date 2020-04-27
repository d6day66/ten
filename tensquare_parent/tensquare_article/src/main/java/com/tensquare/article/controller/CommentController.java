package com.tensquare.article.controller;

import com.tensquare.article.pojo.Comment;
import com.tensquare.article.service.CommentService;
import com.tensquare.entity.Result;
import com.tensquare.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CommentService commentService;

    //    查询 GET/comment
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        List<Comment> list = commentService.findAll();
        return new Result(true, StatusCode.OK, "查询成功", list);
    }

    //    根据评论id查询 GET/comment
    @RequestMapping(value = "{commentId}", method = RequestMethod.GET)
    public Result findById(@PathVariable String commentId) {
        Comment comment = commentService.findById(commentId);
        return new Result(true, StatusCode.OK, "查询成功", comment);
    }

    //新增  post
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Comment comment) {
        commentService.save(comment);
        return new Result(true, StatusCode.OK, "新增成功");
    }

    //    修改评论 put
    @RequestMapping(value = "{commentId}", method = RequestMethod.PUT)
    public Result updateById(@PathVariable String commentId, @RequestBody Comment comment) {
//        设置评论主键
        comment.set_id(commentId);
        commentService.updateById(comment);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    //    删除
    @RequestMapping(value = "{commentId}", method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String commentId) {
        commentService.deleteById(commentId);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    //    根据文章id查询评论
    @RequestMapping(value = "article/{articleId}", method = RequestMethod.GET)
    public Result findByArticleId(@PathVariable String articleId) {
        List<Comment> list = commentService.findByArticleId(articleId);
        return new Result(true, StatusCode.OK, "查询成功", list);
    }

    //点赞
    @RequestMapping(value = "thumbup/{comentId}", method = RequestMethod.PUT)
    public Result thumbup(@PathVariable String comentId) {
//        增加判断redis值来防止重复点赞
//        增加用户
        String userid = "123";
        Object result = redisTemplate.boundValueOps("thumbup_" + userid + "_" + comentId).get();
        if (result != null) {
            return new Result(false, StatusCode.REMOTEERROR, "不能重复点赞");
        } else {
            commentService.thumbup(comentId);
            redisTemplate.opsForValue().set("thumbup_" + userid + "_" + comentId,1);
            return new Result(true, StatusCode.OK, "点赞成功");
        }

    }


}
