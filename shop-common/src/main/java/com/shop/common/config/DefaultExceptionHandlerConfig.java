package com.shop.common.config;

import com.shop.common.exception.ShopBindException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 自定义错误处理器
 * @author yzh
 */
@Controller
@RestControllerAdvice
public class DefaultExceptionHandlerConfig {


    @ExceptionHandler(BindException.class)
    public ResponseEntity<String> bindExceptionHandler(BindException e){
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e){
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(ShopBindException.class)
    public ResponseEntity<String> unauthorizedExceptionHandler(ShopBindException e){
        e.printStackTrace();
        return ResponseEntity.status(e.getHttpStatusCode()).body(e.getMessage());
    }
}