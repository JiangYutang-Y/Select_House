package com.selecthome.controller;

import com.selecthome.base.ApiResponse;
import com.selecthome.service.SmsService;
import com.selecthome.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sms")
public class SmsController {
    private final UserService userService;

    public SmsController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/request_verification")
    public ApiResponse<Object> requestVerification(@RequestParam(value = "phoneNumber") String phoneNumber) {
        userService.requestVerification(phoneNumber);
        return ApiResponse.success();
    }
}
