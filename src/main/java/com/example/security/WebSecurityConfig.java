package com.example.security;

import javax.inject.Inject;

import com.example.Entity.JwtAuthenticationEntryPoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by chenlu on 2017/11/23.
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Inject
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Inject
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Inject
    private UserDetailsService userDetailsService;

    //@Autowired
//    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        authenticationManagerBuilder
//                // 设置UserDetailsService
//                .userDetailsService(this.userDetailsService)
//                // 使用BCrypt进行密码的hash
//                .passwordEncoder(passwordEncoder());
//    }
    // 装载BCrypt密码编码器
    //@Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    protected void configure(HttpSecurity httpSecurity)throws Exception{
        httpSecurity.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers(
                        HttpMethod.GET,
                "/",
                "/*.html",
                "/favicon.ico",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js"
                ).permitAll()
                .antMatchers("/auth/**")
                .permitAll()
                .anyRequest().authenticated();
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.headers().cacheControl();
    }
}
