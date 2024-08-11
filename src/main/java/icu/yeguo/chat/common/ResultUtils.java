package icu.yeguo.chat.common;

import java.io.Serializable;

public class ResultUtils implements Serializable {

    public static <T> Result<T> success(T data) {
        return new Result<>(data);
    }

    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message);
    }
}

