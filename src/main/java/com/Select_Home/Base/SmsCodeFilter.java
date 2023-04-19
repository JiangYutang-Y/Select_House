package com.Select_Home.Base;

import com.Select_Home.Entity.User;
import com.Select_Home.Repository.RoleRepository;
import com.Select_Home.Repository.UserRepository;
import com.Select_Home.TencentCloud.TencentSmsSender;
import com.Select_Home.TencentCloud.TencentSmsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

/*
* POST /api/sessions?method=sms : 表示手机验证码登录请求
* Power Shell
* Invoke-WebRequest -Uri "http://localhost:8081/api/sessions?method=sms" -Method POST -Body "phone=131000&code=123456"
* */
@Component
public class SmsCodeFilter extends AbstractAuthenticationProcessingFilter {
   // private static final String DEFAULT_CODE = "123456";
    private static final RequestMatcher DEFAULT_ANT_PATH_MATCHER = new AntPathRequestMatcher("/api/sessionsSms","POST");

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    private TencentSmsService tencentSmsService;

    public SmsCodeFilter(UserRepository userRepository, RoleRepository roleRepository,ObjectMapper mapper){
        super(DEFAULT_ANT_PATH_MATCHER);
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        super.setAuthenticationSuccessHandler((request, response, authentication) -> {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(mapper.writeValueAsString(ApiResponse.success()));
            response.getWriter().flush();
        });
        super.setAuthenticationFailureHandler((request, response, exception) -> {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(mapper.writeValueAsString(ApiResponse.fail(Status.PHONE_CODE_FAIL)));
            response.getWriter().flush();
        });
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return super.requiresAuthentication(request, response) && checkAuthorizationMethod(request);
    }

    private boolean checkAuthorizationMethod(HttpServletRequest request){
        String method = request.getParameter("method");
        return "sms".equals(method) ;
    }

    @Override
    public void afterPropertiesSet() {
        //没用到authentication Manager ,不重写会报错
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String phone = request.getParameter("phone");
        phone = (phone != null) ? phone.trim() : "";//去掉首尾字符
        String code = request.getParameter("code");
        code = (code != null) ? code.trim() : "";//去掉首尾字符
//      SmsCodeAuthenticationToken token = new SmsCodeAuthenticationToken(phone); 获取未授权Token
        User user = userRepository.findByPhoneNumber(phone);
        if(user == null){

            throw new UsernameNotFoundException("未找到用户");
        }

        //校验验证码
        String storedCode = tencentSmsService.getSmsCode(phone);
        if (storedCode == null || !storedCode.equals(code)) {
            throw new BadCredentialsException("验证码错误");
        }

        /*if(!storedCode.equals(code)){
            throw new BadCredentialsException("验证码错误");
        }*/
        return new SmsCodeAuthenticationToken(new SecurityUser(user,roleRepository.findAllByUserId(user.getId())));
    }
}
