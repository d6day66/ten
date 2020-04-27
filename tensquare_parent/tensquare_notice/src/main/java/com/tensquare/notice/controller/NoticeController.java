package com.tensquare.notice.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.tensquare.entity.PageResult;
import com.tensquare.entity.Result;
import com.tensquare.entity.StatusCode;
import com.tensquare.notice.pojo.Notice;
import com.tensquare.notice.pojo.NoticeFresh;
import com.tensquare.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;
//1. 根据id查询消息通知
//2. 根据条件分页查询消息通知
//3. 新增通知
//4. 修改通知
//5. 根据用户id查询该用户的待推送消息（新消息）
//6. 删除待推送消息（新消息）

    //    根据id查询消息通知
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        Notice notice = noticeService.findById(id);
        return new Result(true, StatusCode.OK, "查询成功");
    }

    //     根据条件分页查询消息通知
    @RequestMapping(value = "/{search/{page}/{size}}", method = RequestMethod.POST)
    public Result selectByList(@PathVariable Integer page,
                               @PathVariable Integer size,
                               @RequestBody Notice notice) {
        Page<Notice> pageData = noticeService.selectPage(page, size, notice);
        PageResult pageResult = new PageResult(pageData.getTotal(), pageData.getRecords());

        return new Result(true, StatusCode.OK, "查询成功", pageResult);
    }

    //     新增通知
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Notice notice) {
        noticeService.save(notice);

        return new Result(true, StatusCode.OK, "新增成功");
    }

    //4. 修改通知
    @RequestMapping(method = RequestMethod.PUT)
    public Result updateById(@RequestBody Notice notice) {
        noticeService.updateById(notice);

        return new Result(true, StatusCode.OK, "修改成功");
    }


    // 5. 根据用户id查询该用户的待推送消息（新消息）
    // http://127.0.0.1:9014/notice/fresh/{userId}/{page}/{size}  GET
    @RequestMapping(value = "fresh/{userId}/{page}/{size}", method = RequestMethod.GET)
    public Result freshPage(@PathVariable String userId,
                            @PathVariable Integer page,
                            @PathVariable Integer size) {
        Page<NoticeFresh> pageData = noticeService.freshPage(userId,page,size);

        PageResult<NoticeFresh> pageResult = new PageResult<>(
                pageData.getTotal(), pageData.getRecords()
        );

        return new Result(true, StatusCode.OK, "查询成功", pageResult);
    }

    // 6. 删除待推送消息（新消息）
    // http://127.0.0.1:9014/notice/fresh  DELETE
    @RequestMapping(value = "fresh", method = RequestMethod.DELETE)
    public Result freshDelete(@RequestBody NoticeFresh noticeFresh) {
        noticeService.freshDelete(noticeFresh);
        return new Result(true, StatusCode.OK, "删除成功");
    }
}
