package com.arthur.sams.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

/**
 * 全局异常处理：把异常转成统一的 JSON，避免前端拿到一大段错误堆栈页面
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorBody notFound(NoSuchElementException e) {
        return new ErrorBody(404, e.getMessage() == null ? "资源不存在" : e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorBody badRequest(IllegalArgumentException e) {
        return new ErrorBody(400, e.getMessage() == null ? "请求参数不合法" : e.getMessage());
    }

    public record ErrorBody(int code, String message) {
    }
}
