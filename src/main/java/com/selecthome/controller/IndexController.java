package com.selecthome.controller;

import com.selecthome.base.BusinessException;
import com.selecthome.base.Status;
import com.selecthome.entity.User;
import com.selecthome.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//首页控制器

@RestController
public class IndexController {
    private UserRepository userRepository;
    private PasswordEncoder encoder;

    public IndexController(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @RequestMapping("/")
    public String HelloWorld(){
        return "Hello World";
    }

    @RequestMapping("/register")
    public void register(String username,String password){
        User user = new User();

        if (password == null) {
            throw new BusinessException(Status.PASSWORD_INVALID);
        }
        if (password.length() < 6) {
            throw new BusinessException(Status.PASSWORD_LENGTH_ERROR);
        }

        user.setName(username);
        user.setPassword(encoder.encode(password));
        userRepository.save(user);

    }

    @RequestMapping("/logi")
        public String Login(){return "Hello World";}
}
