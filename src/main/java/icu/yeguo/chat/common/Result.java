package icu.yeguo.chat.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    private int code;
    private T data;
    private String message;

    public Result(T data) {
        this.code = ResponseCode.SUCCESS.getCode();
        this.data = data;
        this.message = ResponseCode.SUCCESS.getMessage();
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
