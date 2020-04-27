package com.tensquare.article.feign;

import com.tensquare.article.pojo.Notice;
import com.tensquare.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("tensquare_notice")
public interface NoticeFeign {

    @RequestMapping(value = "/notice",method = RequestMethod.POST)
    public Result save(@RequestBody Notice notice);
}
