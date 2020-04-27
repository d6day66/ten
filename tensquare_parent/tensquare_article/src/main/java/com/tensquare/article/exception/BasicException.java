package com.tensquare.article.exception;

import com.tensquare.entity.Result;
import com.tensquare.entity.StatusCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BasicException {
    @ExceptionHandler(Exception.class)
    public Result basicException(Exception e){
        e.printStackTrace();
        return new Result(false, StatusCode.ERROR,"执行出错");

    }
}
