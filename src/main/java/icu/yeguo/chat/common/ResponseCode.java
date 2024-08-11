package icu.yeguo.chat.common;

import lombok.Getter;

@Getter
public enum ResponseCode {

    SUCCESS(20000,"成功！"),
    PARAMS_ERROR(40000, "请求参数错误！"),
    USER_NOT_EXIST_ERROR (40001, "用户不存在！"),
    MAILBOX_FORMAT_ERROR(40002, "邮箱格式错误！"),
    TIME_OUT(40003, "超时！"),
    PASSWD_RULE_ERROR(40004, "密码不符合规则！"),
    PASSWD_DIFFERENT_ERROR(40005, "两次密码不相同！"),
    USERNAME_EXIST_ERROR (40006, "用户名已存在！"),
    MAILBOX_EXIST_ERROR (40007, "邮箱已存在！"),
    PASSWORD_LENGTH_ERROR (40008, "密码长度不得小于8！"),
    PASSWD_ERROR(40009,"密码错误！"),
    NOT_LOGIN_ERROR(40100,"未登录！"),
    NO_PERMISSION_ERROR (40101,"无权限！"),
    FORBIDDEN_ERROR(40300,"禁止访问！"),
    NOT_FOUND_ERROR(40400, "请求数据不存在！"),
    SYSTEM_ERROR(50000, "系统内部异常！"),
    OPERATION_ERROR(50001, "操作失败！"),
    REGISTER_ERROR(50002, "注册失败！");

    private final int code;
    private final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
