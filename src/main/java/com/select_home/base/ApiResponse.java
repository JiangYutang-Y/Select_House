package com.Select_Home.Base;

public class ApiResponse<T> {
    public static <T> ApiResponse<T> success(){
        return new ApiResponse<>(Status.SUCCESS,null);
    }

    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse(Status.SUCCESS,data);
    }

    public static ApiResponse<Object> fail(Status status){
        return new ApiResponse(status,null);
    }

    /*public static <T> ApiResponse<T> fail(T data){
        return new ApiResponse(Status.CODE_FAIL,data);
    }*/

    //错误码
    private int code;

    //错误描述
    private String message;

    //响应数据
    private T data;

    public ApiResponse(Status status,T data){
        this.code = status.getCode();
        this.message = status.getMessage();
        this.data = data;
    }

    ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
