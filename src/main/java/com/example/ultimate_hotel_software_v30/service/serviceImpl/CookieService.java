package com.example.ultimate_hotel_software_v30.service.serviceImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    @Value("${refresh.expiration}")
    private Long expiration;

    private final String COOKIE_NAME = "refreshToken";

    public ResponseCookie createRefreshTokenCookie(String refreshToken){
        return ResponseCookie.from(COOKIE_NAME,refreshToken)
                .path("/public/auth")
                .maxAge(expiration/1000)
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .build();
    }

    public ResponseCookie cleanRefreshTokenCookie(){
        return ResponseCookie.from(COOKIE_NAME,"")
                .path("/public/auth")
                .maxAge(0)
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .build();
    }

}
