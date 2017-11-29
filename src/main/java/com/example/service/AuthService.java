package com.example.service;

import java.util.List;

import com.example.Entity.User;

import org.springframework.stereotype.Service;

/**
 * Created by chenlu on 2017/11/23.
 */
@Service
public interface AuthService {
    User register(User userAdd);
    String login(String username,String password);
    String refresh(String oldToken);
    User getUserByName(String name);
    List<User> findAll();
}
/****test!!!*/