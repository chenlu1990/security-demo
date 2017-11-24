package com.example.service;

import java.util.ArrayList;
import java.util.List;

import com.example.Entity.Authority;
import com.example.Entity.JwtUserFactory;
import com.example.Entity.User;
import com.example.dao.AuthorityRepository;
import com.example.dao.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by chenlu on 2017/11/23.
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findUserByName(s);
        List<Authority> authorizations = authorityRepository.findAll();
        user.setAuthorities(authorizations);
        if(user == null){
            throw new UsernameNotFoundException(String.format("No user found width name %s",s));
        }else{
            return JwtUserFactory.create(user);
        }
    }
}
