package com.example.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.example.Entity.JwtUser;
import com.example.Entity.User;
import com.example.dao.UserRepository;
import com.example.utils.JwtTokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Created by chenlu on 2017/11/23.
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Inject
    private AuthenticationManager authenticationManager;

    @Inject
    private UserDetailsService userDetailsService;

    @Inject
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public User register(User userAdd) {
        final String username = userAdd.getName();
        if (userRepository.findUserByName(username) != null) {
            return null;
        }
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        final String rawPassword = encoder.encode(userAdd.getPassword());
        userAdd.setPassword(userAdd.getPassword());
        userAdd.setLastPasswordReset(new Date());
        userAdd.setAuthorities(new ArrayList<>());
        return userRepository.save(userAdd);
    }

    @Override
    public String login(String username, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String token = jwtTokenUtil.generateToken((JwtUser) userDetails);
        return token;
    }

    @Override
    public String refresh(String oldToken) {
        String username = jwtTokenUtil.getUsernameFromToken(oldToken);
        JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.canTokenBeRefreshed(oldToken, jwtUser.getLastPasswordResetDate())) {
            return jwtTokenUtil.refreshToken(oldToken);
        }
        return null;
    }

    @Override
    public User getUserByName(String username) {
        return userRepository.findUserByName(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
