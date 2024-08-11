package icu.yeguo.chat.common;

import icu.yeguo.chat.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public <T> Result<T> businessExceptionHandler(BusinessException e) {
        log.error("业务异常-->{}:{}", e.getClass(), e.getMessage());
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public <T> Result<T> runtimeExceptionHandler(RuntimeException e) {
        log.error("运行时异常-->{}:{}:{}", e.getClass(), e.getMessage(), e.getStackTrace());
        return ResultUtils.error(500, e.getMessage());
    }
}
