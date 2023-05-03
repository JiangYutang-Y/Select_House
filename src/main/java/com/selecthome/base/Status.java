package com.selecthome.base;

public enum Status {
    SUCCESS(0,"SUCCESS"),
    UNKNOWN(1,"未知异常"),
    USERNAME_INVALID(102,"非法用户名"),
    PASSWORD_INVALID(103,"密码不能为空"),
    PASSWORD_LENGTH_ERROR(104,"密码长度不能低于六位"),
    USERNAME_PASSWORD_INVALID(105,"用户名或密码错误"),
    PHONE_CODE_FAIL(106,"手机号或验证码错误"),
    PHONE_NOT_FOUND(107,"手机号未找到"),

    CITY_EN_NAME_INVALID(1000, "城市不能为空")
    ;

    private final int code;
    private final String message;

    Status(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
