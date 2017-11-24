package com.example.controller;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.example.Entity.JwtAuthenticationResponse;
import com.example.Entity.User;
import com.example.service.AuthService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by chenlu on 2017/11/23.
 */
@RestController
public class AuthController {
    @Value("${jwt.header}")
    private String tokenHeader;

    @Inject
    private AuthService authService;

    @PostMapping(value = "${jwt.route.authentication.register}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User register(@RequestBody User addUser) {
        return authService.register(addUser);
    }

    @GetMapping(value = "${jwt.route.authentication.path}")
    public ResponseEntity<JwtAuthenticationResponse> createAuthenticationToken(String username, String password) {
        String token = authService.login(username, password);
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    @GetMapping(value = "${jwt.route.authentication.refresh}")
    public ResponseEntity<JwtAuthenticationResponse> refreshToken(HttpServletRequest request) {
        String oldToken = request.getHeader(tokenHeader);
        if (oldToken == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(new JwtAuthenticationResponse(authService.refresh(oldToken)));
        }
    }

    @PostAuthorize("returnObject.name == principal.username or hasRole('ROLE_ADMIN')")
    @GetMapping("/{name}")
    public User getUser(@PathVariable String name){
        return authService.getUserByName(name);
    }
    @GetMapping
    public ResponseEntity<List<User>> findAll(){
        return ResponseEntity.ok(authService.findAll());
    }
}
