package com.example.ultimate_hotel_software_v30.mapper;

import com.example.ultimate_hotel_software_v30.dto.response.RefreshTokenDTOResponse;
import com.example.ultimate_hotel_software_v30.model.RefreshTokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper {

    @Mapping(target = "accessToken", source = "token")
    @Mapping(target = "tokenType", constant = "Bearer")
    RefreshTokenDTOResponse toRefreshTokenDTOResponse(RefreshTokenEntity entity);

}