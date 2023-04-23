package com.select_home.TencentCloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class TencentSmsService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private TencentSmsSender smsSender;

    public void sendSms(String phone) {
        // 生成验证码
        String code = String.format("%06d", new Random().nextInt(1000000));
        // 调用腾讯云短信API发送验证码
        smsSender.sendSmsCode(phone, code);

        // 存储验证码到Redis，设置有效时间为10分钟
        redisTemplate.opsForValue().set("sms:" + phone, code, 10, TimeUnit.MINUTES);
    }

    public String getSmsCode(String phone) {
        return redisTemplate.opsForValue().get("sms:" + phone);
    }
}
