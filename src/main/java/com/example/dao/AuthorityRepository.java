package com.example.dao;

import com.example.Entity.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by chenlu on 2017/11/24.
 */
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
}
