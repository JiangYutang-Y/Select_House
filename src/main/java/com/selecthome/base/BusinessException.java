package com.selecthome.base;

public class BusinessException extends RuntimeException{

    //错误码
    private int code = 1;//通用异常错误码

    public BusinessException(String message){
        super(message);
    }

    public BusinessException(Status status){
        super(status.getMessage());
        this.code = status.getCode();
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(Exception e) {
        super(e);
    }

    public int getCode() {
        return this.code;
    }
}
