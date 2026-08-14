package com.example.ultimate_hotel_software_v30.service.serviceImpl;

import com.example.ultimate_hotel_software_v30.exceptions.InvalidTokenException;
import com.example.ultimate_hotel_software_v30.model.RefreshTokenEntity;
import com.example.ultimate_hotel_software_v30.model.UserEntity;
import com.example.ultimate_hotel_software_v30.repository.RefreshTokenRepository;
import com.example.ultimate_hotel_software_v30.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${refresh.expiration}")
    private Long expiration;

    public RefreshTokenEntity createRefreshToken(String username){

        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("incorrect user"));

        return RefreshTokenEntity.builder()
                .token(UUID.randomUUID().toString()) //generacion de token aleatorio
                .expireDate(Instant.now().plusMillis(expiration))
                .userEntity(userEntity)
                .build();
    }

    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity refreshTokenEntity){
        if(refreshTokenEntity.getExpireDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(refreshTokenEntity);
            throw new InvalidTokenException("Refresh token is expired. Please login again");
        }
        return refreshTokenEntity;
    }

    public void deleteRefreshTokenByUsername(String username){
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("incorrect user"));
        refreshTokenRepository.deleteByUserEntity(userEntity);
    }
}
