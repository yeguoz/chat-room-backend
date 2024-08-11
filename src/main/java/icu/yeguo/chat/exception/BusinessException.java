package icu.yeguo.chat.exception;


import icu.yeguo.chat.common.ResponseCode;
import lombok.Getter;

/**
 * 业务异常类
 */
@Getter
public class BusinessException extends RuntimeException{

    private final int code;

    public BusinessException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.code = responseCode.getCode();
    }
}
