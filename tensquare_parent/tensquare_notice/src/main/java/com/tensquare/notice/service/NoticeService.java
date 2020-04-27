package com.tensquare.notice.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tensquare.entity.Result;
import com.tensquare.notice.dao.NoticeDao;
import com.tensquare.notice.dao.NoticeFreshDao;
import com.tensquare.notice.feign.ArticleFeign;
import com.tensquare.notice.feign.UserFeign;
import com.tensquare.notice.pojo.Notice;
import com.tensquare.notice.pojo.NoticeFresh;
import com.tensquare.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class NoticeService {
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private NoticeDao noticeDao;
    @Autowired
    private NoticeFreshDao noticeFreshDao;
    @Autowired
    private ArticleFeign articleFeign;
    @Autowired
    private UserFeign userFeign;

    //    完善消息
    public void noticInfo(Notice notice) {
        String articleId = notice.getTargetId();
        String userId = notice.getOperatorId();
        Result articleFeignById = articleFeign.findById(articleId);
        HashMap articleMap = (HashMap) articleFeignById.getData();

        Result userFeignById = userFeign.findById(userId);
        HashMap userMap = (HashMap) userFeignById.getData();

        notice.setTargetId(articleMap.get("title").toString());
        notice.setOperatorId(userMap.get("nickname").toString());

    }

    public Notice findById(String id) {
        Notice notice = noticeDao.selectById(id);
        noticInfo(notice);
        return notice;
    }

    //分页查询
    public Page<Notice> selectPage(Integer page, Integer size, Notice notice) {
        EntityWrapper<Notice> wrapper = new EntityWrapper<>(notice);
        Page<Notice> pagedata = new Page<>(page, size);

        List<Notice> notices = noticeDao.selectPage(pagedata, wrapper);
        for (Notice n : notices) {
            noticInfo(n);
        }
        pagedata.setRecords(notices);
        return pagedata;
    }

    //新增
    public void save(Notice notice) {
        String id = idWorker.nextId() + "";
        notice.setId(id);
        notice.setState("0");
        notice.setCreatetime(new Date());
        noticeDao.insert(notice);
//        待提送消息
        NoticeFresh noticeFresh = new NoticeFresh();
        noticeFresh.setNoticeId(id);
        noticeFresh.setUserId(notice.getReceiverId());
        noticeFreshDao.insert(noticeFresh);

    }

    //修改
    public void updateById(Notice notice) {
        noticeDao.updateById(notice);
    }

    public void freshDelete(NoticeFresh noticeFresh) {
        noticeFreshDao.delete(new EntityWrapper<NoticeFresh>(noticeFresh));
    }

    public Page<NoticeFresh> freshPage(String userId, Integer page, Integer size) {

        Page<NoticeFresh> noticeFreshPage = new Page<>(page, size);
        List<NoticeFresh> noticeFreshes = noticeFreshDao.selectPage(noticeFreshPage, new EntityWrapper<NoticeFresh>().eq("userId", userId));
        noticeFreshPage.setRecords(noticeFreshes);
        return noticeFreshPage;
    }
}
