package com.example.ultimate_hotel_software_v30.mapper;

import com.example.ultimate_hotel_software_v30.dto.request.ItemDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.ItemDTOResponse;
import com.example.ultimate_hotel_software_v30.model.ItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "id", ignore = true)
    ItemEntity toItemEntity(ItemDTORequest request);

    ItemDTOResponse toItemDTOResponse(ItemEntity entity);

}