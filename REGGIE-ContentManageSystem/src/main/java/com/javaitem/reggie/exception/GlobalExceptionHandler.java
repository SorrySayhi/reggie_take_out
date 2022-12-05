package com.javaitem.reggie.exception;


import com.reggie.util.CommonResult;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;


/**
 * @Author win
 * @create 2022/11/24 2:07
 */
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public CommonResult exceptionHandler(SQLIntegrityConstraintViolationException exception){
        String message = exception.getMessage();
        boolean duplicateEntry = message.contains("Duplicate entry");
        if (duplicateEntry){

            return CommonResult.error("名称已存在");
        }
        return CommonResult.error("请稍后重试");
    }

}
