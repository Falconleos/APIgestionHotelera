package com.example.ultimate_hotel_software_v30.mapper;

import com.example.ultimate_hotel_software_v30.dto.request.RoomAttentionDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.RoomAttentionDTOResponse;
import com.example.ultimate_hotel_software_v30.model.RoomAttentionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ItemMapper.class})
public interface RoomAttentionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookingEntity", ignore = true)
    @Mapping(target = "item", ignore = true)
    @Mapping(target = "unitPrice", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "employeeEntity", ignore = true)
    RoomAttentionEntity toRoomAttentionEntity(RoomAttentionDTORequest request);

    @Mapping(target = "bookingId", source = "bookingEntity.id")
    @Mapping(target = "itemId", source = "item.id")
    @Mapping(target = "itemDTOResponse", source = "item")
    @Mapping(target = "isService", source = "item.isService")
    @Mapping(target = "subtotal", expression = "java(entity.getSubtotal())")
    @Mapping(target = "employeeUsername", source = "employeeEntity.userEntity.username")
    RoomAttentionDTOResponse toRoomAttentionDTOResponse(RoomAttentionEntity entity);

}