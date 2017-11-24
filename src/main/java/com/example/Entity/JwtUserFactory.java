package com.example.Entity;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by chenlu on 2017/11/23.
 */

public final class JwtUserFactory {

    private JwtUserFactory(){

    }
    public static JwtUser create(User user){
        return new JwtUser(String.valueOf(user.getId()),
                user.getName(),
                user.getPassword(),
                user.getEmail(),
                mapToGrantedAuthorities(user.getAuthorities()),
                user.getLastPasswordReset());
    }
    public static List<GrantedAuthority> mapToGrantedAuthorities(List<Authority> authorities){
        return authorities.stream().map(authority -> new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return authority.getName();
            }
        }).collect(Collectors.toList());
    }
}
