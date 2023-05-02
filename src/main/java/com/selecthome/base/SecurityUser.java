package com.selecthome.base;

import com.selecthome.entity.Role;
import com.selecthome.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SecurityUser implements UserDetails {
    private User user;
    private List<Role> roles;

    public SecurityUser(User user, List<Role> roles) {
        this.user = user;
        this.roles = roles;
    }

    public User getUser() {
        return this.user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { //用户权限
        return roles.stream()
                .flatMap(r -> Stream.concat(
                        Stream.of("ROLE_" + r.getName()), // 赋予角色
                        Stream.of(r.getName()) // 赋予权限
                )).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        //        return Collections.emptyList();//权限设空
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    @Override
    public boolean isAccountNonExpired() { //用户账户封禁
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {//用户账号锁定
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { //用户在规定时间内需更改
        return true;
    }

    @Override
    public boolean isEnabled() { //注销用户
        return true;
    }
}
