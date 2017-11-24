package com.example.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.Entity.JwtUser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Created by chenlu on 2017/11/23.
 */
@Component
public class JwtTokenUtil implements Serializable {
    private static final long serialVersionUID = 2151030343391408476L;

    static final String CLAIM_KEY_USERNAME = "sub";
    static final String CLAIM_KEY_AUDIENCE = "audience";
    static final String CLAIM_KEY_CREATED = "created";

    private static final String AUDIENCE_UNKNOWN = "unknown";
    private static final String AUDIENCE_WEB = "web";
    private static final String AUDIENCE_MOBILE = "mobile";
    private static final String AUDIENCE_TABLET = "tablet";

    @Value("${jwt.secret}")
    private String secret;
//    @Value("${jwt.expiraion}")
//    private String expiraion;

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    public String generateToken(JwtUser userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Date getCreatedFromToken(String token){
        Date date;
        try{
           Claims claims = getClaimsFromToken(token);
            date = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        }catch(Exception e){
            date = null;
        }
        return date;
    }
    public Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset){
        return created!=null && created.before(lastPasswordReset);
    }

    public Boolean validateToken(String token,UserDetails userDetails){
        JwtUser jwtUser = (JwtUser) userDetails;
        String username = getUsernameFromToken(token);
        Date date = getCreatedFromToken(token);
        return username.equals(jwtUser.getUsername()) && !isCreatedBeforeLastPasswordReset(date,jwtUser.getLastPasswordResetDate());
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getCreatedFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset);
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }
    String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

}
