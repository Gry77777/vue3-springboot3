package com.example.springboot.execption;

import com.example.springboot.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice("com.example.springboot.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    @ResponseBody //返回json格式的
    public Result error(CustomException e) {
        e.printStackTrace();
        return Result.error(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody //返回json格式的
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.error();
    }

}
