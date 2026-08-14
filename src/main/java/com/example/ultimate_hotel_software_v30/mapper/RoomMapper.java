package com.example.ultimate_hotel_software_v30.mapper;

import com.example.ultimate_hotel_software_v30.dto.request.RoomDTORequest;
import com.example.ultimate_hotel_software_v30.dto.request.RoomUpdateDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.RoomDTOResponse;
import com.example.ultimate_hotel_software_v30.model.RoomEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RoomTypeMapper.class})
public interface RoomMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "images", ignore = true)
    RoomEntity toRoomEntity(RoomDTORequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "images", ignore = true)
    RoomEntity toRoomEntityFromUpdate(RoomUpdateDTORequest request);

    @Mapping(target = "roomTypeDTOResponse", source = "type")
    @Mapping(target = "imagesCount", expression = "java(entity.getImages() != null ? entity.getImages().size() : 0)")
    RoomDTOResponse toRoomDTOResponse(RoomEntity entity);

}