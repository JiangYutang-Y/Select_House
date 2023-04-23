package com.selecthome.base;

import com.selecthome.entity.Role;
import com.selecthome.entity.User;
import com.selecthome.repository.RoleRepository;
import com.selecthome.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

//声明是一个配置类
@Configuration
@EnableWebSecurity
public class WebSecurityConfig{

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private ObjectMapper mapper;
    private SmsCodeFilter filter;
    /* 序列化
    private  static final String SUCCESS_RESPONSE;
    static {
        try {
            SUCCESS_RESPONSE = new ObjectMapper().writeValueAsString(ApiResponse.success());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }*/

    public WebSecurityConfig(UserRepository userRepository, RoleRepository roleRepository, ObjectMapper mapper, SmsCodeFilter filter) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.mapper = mapper;
        this.filter = filter;
    }

    //DI:依赖注入(控制反转):从IOC容器中寻找是否有对应的类的实例，有的话帮我们实现出来，如果没有就会报错
    /*public WebSecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }*/

    @Bean
    public SecurityFilterChain getSecurity(HttpSecurity http) throws Exception{
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);//先处理手机号验证码在处理用户名密码登录
        return http.authorizeHttpRequests(new Customizer<AuthorizeHttpRequestsConfigurer<org.springframework.security.config.annotation.web.builders.HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>() {
            @Override
            public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
                registry
                        .antMatchers("/admin/**").hasRole("admin") // /admin路径下的访问必须登录并且具有 admin 角色
                        .antMatchers("/user/**").authenticated() //  /users 路径下的访问都是必须登录的
                        .antMatchers("/api/users/sms").permitAll()
                        .anyRequest().permitAll(); // 所有的url路径都是可以直接进行访问的  黑名单模式
            }
        }).userDetailsService(userDetailsService())
                //formLogin -> UserNamePasswordAuthenticationFilter
                //@see FormLoginConfigurer
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.loginProcessingUrl("/api/sessions")
                        .successHandler(((request, response, authentication) -> {
                            response.setStatus(HttpStatus.OK.value());
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write(mapper.writeValueAsString(ApiResponse.success()));
                            response.getWriter().flush();
                        }))
                        .failureHandler(((request, response, exception) -> {
                            response.setStatus(HttpStatus.OK.value());
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write(mapper.writeValueAsString(ApiResponse.fail(Status.USERNAME_PASSWORD_INVALID)));
                            response.getWriter().flush();
                        })))//.and() //启用登录页面
                .csrf().disable() //FROM表单 会出现跨站攻击 spring security 默认开启，前后端分离不会有这种方式
                .build();
    }

    @Bean
    public  UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
               User user = userRepository.findByName(username);
               if(user == null){
                   throw new UsernameNotFoundException("用户名或密码错误");
               }
                List<Role> roles = roleRepository.findAllByUserId(user.getId());
                return new SecurityUser(user,roles);
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /*@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/
}
