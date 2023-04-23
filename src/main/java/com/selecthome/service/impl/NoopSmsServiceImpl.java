package com.selecthome.service.impl;

import com.selecthome.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "app.sms", name = "provider", havingValue = "none", matchIfMissing = true)
public class NoopSmsServiceImpl implements SmsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoopSmsServiceImpl.class);

    public NoopSmsServiceImpl() {
        LOGGER.warn("********************当前短信验证码服务处于开发模式，不会真实发送短信，登陆时请注意控制台验证码输出**********************");
    }

    @Override
    public void sendSms(String phone, String code) {
        LOGGER.info("[仅开发环境]手机号：{}, 验证码：{}", phone, code);
    }
}
