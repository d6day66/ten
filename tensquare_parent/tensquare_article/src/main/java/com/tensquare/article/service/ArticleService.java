package com.tensquare.article.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tensquare.article.dao.ArticleMapper;
import com.tensquare.article.feign.NoticeFeign;
import com.tensquare.article.pojo.Article;
import com.tensquare.article.pojo.Notice;
import com.tensquare.util.IdWorker;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ArticleService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private NoticeFeign noticeFeign;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private IdWorker idWorker;

    //    新增文章
    public void save(Article article) {
//        通过jwt鉴权获取用户id,先造个数据用下
        String userId = "3";

        String id = idWorker.nextId() + "";
        article.setUserid(userId);
        article.setId(id);
        article.setComment(0);
        article.setVisits(0);
        article.setThumbup(0);
        articleMapper.insert(article);
        Set<String> set = redisTemplate.boundSetOps("article_author_" + userId).members();
        for (String uid : set) {
            // 创建消息对象
            Notice notice = new Notice();

            // 接收消息用户的ID
            notice.setReceiverId(uid);
            // 进行操作用户的ID
            notice.setOperatorId(userId);
            // 操作类型（评论，点赞等）
            notice.setAction("publish");
            // 被操作的对象，例如文章，评论等
            notice.setTargetType("article");
            // 被操作对象的id，例如文章的id，评论的id'
            notice.setTargetId(id);
            // 通知类型
            notice.setType("sys");

            noticeFeign.save(notice);

        }

    }

    //删除文章
    public void deleteById(String articleId) {
        articleMapper.deleteById(articleId);
    }

    //修改文章
    public void updateById(Article article) {
        articleMapper.updateById(article);
//        EntityWrapper<Article> wrapper = new EntityWrapper<>();
//        wrapper.eq("id",articleId);
//        articleMapper.update(article,wrapper);
    }

    public Page<Article> findByPage(Integer page, Integer size, Map<String, Object> map) {

        Page<Article> pageData = new Page<>(page, size);
        EntityWrapper<Article> wrappper = new EntityWrapper<>();
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            wrappper.eq(map.get(key) != null, key, map.get(key));
        }
        List<Article> list = articleMapper.selectPage(pageData, wrappper);
        pageData.setRecords(list);
        return pageData;
    }

    public Article findById(String articleId) {

        Article article = articleMapper.selectById(articleId);
        return article;
    }

    public Boolean subscribe(String userId, String articleId) {
//        作者id
        Article article = articleMapper.selectById(articleId);
        String authorId = article.getUserid();

//        1.创建rabit管理器
        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate.getConnectionFactory());
//        2.声明交换机，处理新增文章消息
        DirectExchange exchange = new DirectExchange("article_subscribe");
        rabbitAdmin.declareExchange(exchange);
//        3.声明队列，每个用户都有自己的队列，通过用户id进行区分
        Queue queue = new Queue("article_subscribe_"+userId);
        rabbitAdmin.declareQueue(queue);
//        4.声明交换机和队列的绑定关系，需要确保队列只接收到对应作者的新增文章消息
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(authorId);

//        读者订阅作者，redis的Key
        String userKey = "article_subscribe_" + userId;
//        作者增加订阅者，redis的key
        String authorKey = "article_author_" + authorId;
//       查询redis看是否有值，无则未订阅，订阅
        Boolean flag = redisTemplate.boundSetOps(userKey).isMember(authorId);

        if (flag) {
//            有订阅，取消订阅
            redisTemplate.boundSetOps(userKey).remove(authorId);
            redisTemplate.boundSetOps(authorKey).remove(userId);
            rabbitAdmin.removeBinding(binding);
            return false;
        } else {
//           未订阅，则订阅
            redisTemplate.boundSetOps(userKey).add(authorId);
            redisTemplate.boundSetOps(authorKey).add(userId);
            rabbitAdmin.declareBinding(binding);
            return true;
        }

    }

    public void thumbup(String articleId,String userId) {
        Article article = articleMapper.selectById(articleId);
        Integer thumbup = article.getThumbup();
        article.setThumbup(thumbup+1);
        articleMapper.updateById(article);
//消息通知
        // 创建消息对象
        Notice notice = new Notice();

        // 接收消息用户的ID
        notice.setReceiverId(article.getUserid());
        // 进行操作用户的ID
        notice.setOperatorId(userId);
        // 操作类型（评论，点赞等）
        notice.setAction("publish");
        // 被操作的对象，例如文章，评论等
        notice.setTargetType("article");
        // 被操作对象的id，例如文章的id，评论的id'
        notice.setTargetId(articleId);
        // 通知类型
        notice.setType("user");

        noticeFeign.save(notice);
    }

//
}
