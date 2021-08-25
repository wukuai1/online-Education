package com.atguigu.servicebase.handler;

import com.atguigu.commonutils.R;
import com.atguigu.commonutils.exception.GuliException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message("执行了全局统一异常处理");
    }


    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public R specialError(ArithmeticException e){
        e.printStackTrace();
        return R.error().message("执行了特定异常异常");
    }


    @ExceptionHandler(GuliException.class)
    @ResponseBody
    public R comError(GuliException e){
        log.error(e.getMessage());
        e.printStackTrace();
        return R.error().message(e.getMessage()).code(e.getCode());
    }

}
