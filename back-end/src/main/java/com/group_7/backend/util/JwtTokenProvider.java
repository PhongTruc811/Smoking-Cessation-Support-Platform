package com.group_7.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private int jwtExpiration;

    //Tạo token cho user
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);//Lấy thời hạn đã set từ app.properties
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret) //Kí token với secret key và generate
                .setIssuer("AntiSmoking.com")
                .compact();
    }

    //Lấy username từ token
    public String getUsernameFromJWT(String token) {
        return getClaimsFromJWT(token).getSubject();
    }

    //Lấy thời gian hết hạn từ token
    public Date getIssuedAtFromJWT(String token) {
        return getClaimsFromJWT(token).getIssuedAt();
    }


    public Claims getClaimsFromJWT(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }

    //Xác nhận token hợp lệ
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (JwtException ex) {
            return false;
        } /*catch (SignatureException ex) {
            // Chữ ký không hợp lệ
        } catch (ExpiredJwtException ex) {
            // Token hết hạn
        } catch (IllegalArgumentException ex) {
            // Chuỗi token trống
        }*/
    }
}