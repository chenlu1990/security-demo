package com.example.dao;


import com.example.Entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Created by chenlu on 2017/11/23.
 */

public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByName(String name);
}
