package com.selecthome.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class ErrorHandlerController {

    //日志输出工具
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandlerController.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiResponse<Object> handle(Exception e){
        LOGGER.error("统一异常处理器捕获到业务异常：",e);
        return ApiResponse.fail(Status.UNKNOWN);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ApiResponse<Object> handleBusinessException(BusinessException ex) {
        LOGGER.debug("统一异常处理器捕获到业务异常(code = " + ex.getCode() + ")：", ex);
        return new ApiResponse<>(ex.getCode(), ex.getMessage(), null);
    }
}
