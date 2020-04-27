package com.tensquare.article.service;

import com.tensquare.article.pojo.Comment;
import com.tensquare.article.repository.CommentRepository;
import com.tensquare.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private MongoRepository mongoRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Comment> findAll() {
        List<Comment> list = commentRepository.findAll();
        return list;
    }

    public Comment findById(String commentId) {
        Comment comment = commentRepository.findById(commentId).get();
        return comment;

    }

//新增评论

    public void save(Comment comment) {
//        分布式id
        String id = idWorker.nextId() + "";
        comment.set_id(id);
//        初始化点赞数据，发布时间等
        comment.setPublishdate(new Date());
        comment.setThumbup(0);
//        保存数据
        commentRepository.save(comment);
    }

    //    修改
    public void updateById(Comment comment) {
//   使用mongorepsitory方法
//        其中save方法，注解如果存在，执行修改，如果不存在就新增
        commentRepository.save(comment);
    }

    //    删除
    public void deleteById(String commentId) {

        commentRepository.deleteById(commentId);
    }

    //    根据文章id查询评论
    public List<Comment> findByArticleId(String articleId) {
        List<Comment> comments = commentRepository.findByArticleid(articleId);
        return comments;

    }

    //点赞
    public void thumbup(String comentId) {
//        Comment comment = commentRepository.findById(comentId).get();
//
//        comment.setThumbup(comment.getThumbup() + 1);
//        commentRepository.save(comment);

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(comentId));
        Update update = new Update();
        update.inc("thumbup", 1);
        mongoTemplate.updateFirst(query, update, "commentdb");

    }
}
