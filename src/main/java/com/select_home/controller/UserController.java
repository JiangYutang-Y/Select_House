package com.Select_Home.Controller;

import com.Select_Home.Base.ApiResponse;
import com.Select_Home.Base.BusinessException;
import com.Select_Home.Base.SecurityUser;

import com.Select_Home.Base.Status;
import com.Select_Home.DTO.UserDTO;
import com.Select_Home.Entity.User;
import com.Select_Home.Repository.RoleRepository;
import com.Select_Home.Repository.UserRepository;
import com.Select_Home.TencentCloud.TencentSmsService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController /*implements InitializingBean*/ {
    private final UserRepository userRepository;

    /*@Autowired*/
    private RoleRepository roleRepository;

    private TencentSmsService tencentSmsService;

    public UserController(UserRepository userRepository,TencentSmsService tencentSmsService){
        this.userRepository = userRepository;
        this.tencentSmsService = tencentSmsService;
    }

    @GetMapping("/info")
    public ApiResponse<UserDTO> getUserInfo(){
        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();//返回当前登录的实体
        User user = userRepository.findById(principal.getUser().getId()).orElseThrow(() -> new NullPointerException("用户[" + principal.getUser().getId() + "]不存在"));
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user,dto);
        return ApiResponse.success(dto);
    }

    @PostMapping("/sms")
    public ApiResponse<Object> sendSms(@RequestParam(value = "phoneNumber") String phoneNumber) {
        tencentSmsService.sendSms(phoneNumber);
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new BusinessException(Status.PHONE_NOT_FOUND);

        }

        return ApiResponse.success();
    }

    /*@Override
    public void afterPropertiesSet() throws Exception {
        if(roleRepository == null) {
            throw new NullPointerException("role repository cannot be find")
        }
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }*/
}
