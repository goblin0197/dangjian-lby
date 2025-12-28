package com.coder.springbootinit.exception;

import com.coder.springbootinit.common.BaseResponse;
import com.coder.springbootinit.common.ErrorCode;
import com.coder.springbootinit.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
*/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public BaseResponse<?> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        // log.error("HttpMessageNotReadableException", e);
        // 获取嵌套异常
        Throwable cause = e.getCause();
        if (cause != null) {
            String message = cause.getMessage();
            // 检查是否是long范围问题
            if (message != null && (message.contains("out of range of long") )) {
                // 返回参数错误
                return ResultUtils.error(ErrorCode.PARAMS_ERROR);
            }
        }
        // 其他情况返回系统错误
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }
}
