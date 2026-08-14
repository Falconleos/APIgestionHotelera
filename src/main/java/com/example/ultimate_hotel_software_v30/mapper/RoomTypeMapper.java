package com.example.ultimate_hotel_software_v30.mapper;

import com.example.ultimate_hotel_software_v30.dto.request.RoomTypeDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.RoomTypeDTOResponse;
import com.example.ultimate_hotel_software_v30.model.RoomTypeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomTypeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rooms", ignore = true)
    RoomTypeEntity toRoomTypeEntity(RoomTypeDTORequest request);

    RoomTypeDTOResponse toRoomTypeDTOResponse(RoomTypeEntity entity);

}