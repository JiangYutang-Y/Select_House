package com.selecthome.controller;

import com.selecthome.base.ApiResponse;
import com.selecthome.base.SecurityUser;
import com.selecthome.dto.UserDTO;
import com.selecthome.entity.User;
import com.selecthome.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/info")
    public ApiResponse<UserDTO> getUserInfo() {
        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();//返回当前登录的实体
        User user = userRepository.findById(principal.getUser().getId()).orElseThrow(() -> new NullPointerException("用户[" + principal.getUser().getId() + "]不存在"));
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);
        return ApiResponse.success(dto);
    }
}
