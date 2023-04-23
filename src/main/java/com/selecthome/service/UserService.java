package com.selecthome.service;

import com.selecthome.entity.User;

public interface UserService {
    void requestVerification(String phone);

    String getVerificationCode(String phone);

    User registerByPhoneNumber(String phone);
}
