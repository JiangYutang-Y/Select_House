package com.selecthome.service.impl;

import com.selecthome.base.SonyflakeIdWorker;
import com.selecthome.entity.Role;
import com.selecthome.entity.User;
import com.selecthome.repository.RoleRepository;
import com.selecthome.repository.UserRepository;
import com.selecthome.service.SmsService;
import com.selecthome.service.UserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    private static final String DEFAULT_PREFIX_SMS_KEY = "sms:";
    private final RedisTemplate<String, String> redisTemplate;
    private final SmsService smsService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private static SonyflakeIdWorker sonyflakeIdWorker = new SonyflakeIdWorker();//生成唯一Id

    public UserServiceImpl(RedisTemplate<String, String> redisTemplate,
                           SmsService smsService, UserRepository userRepository, RoleRepository roleRepository) {
        this.redisTemplate = redisTemplate;
        this.smsService = smsService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    private String getSmsKey(String phone) {
        return DEFAULT_PREFIX_SMS_KEY + phone;
    }

    @Override
    public void requestVerification(String phone) {
        // 生成验证码
        String code = generateCode();
        // 调用腾讯云短信API发送验证码
        smsService.sendSms(phone, code);
        // 存储验证码到Redis，设置有效时间为10分钟
        redisTemplate.opsForValue().set(getSmsKey(phone), code, 10, TimeUnit.MINUTES);
    }

    @Override
    public String getVerificationCode(String phone) {
        return redisTemplate.opsForValue().get(getSmsKey(phone));
    }

    @Override
    public User registerByPhoneNumber(String phone) {
        User user = new User();
//        user.setId(System.currentTimeMillis());
        user.setId(sonyflakeIdWorker.nextId());
        user.setName(phone);
        user.setPhoneNumber(phone);
        user.setCreateTime(new Date());
        Role r = new Role();
        r.setName(Role.USER);
//        r.setId(System.currentTimeMillis());
        r.setId(sonyflakeIdWorker.nextId());
        r.setUserId(user.getId());
        roleRepository.save(r);
        return userRepository.save(user);
    }


    private String generateCode() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 1000));
    }
}
