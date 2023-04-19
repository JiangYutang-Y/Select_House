package com.Select_Home.Base;

import com.Select_Home.Entity.Role;
import com.Select_Home.Entity.User;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SecurityUser implements UserDetails {
    private User user;
    private List<Role> roles;

    public SecurityUser(User user,List<Role> roles) {
        this.user = user;
        this.roles = roles;
    }

    public User getUser(){
        return this.user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { //用户权限
        return this.roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
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
