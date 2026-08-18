package com.example.ultimate_hotel_software_v30.mapper;

import com.example.ultimate_hotel_software_v30.dto.request.RoleDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.RoleDTOResponse;
import com.example.ultimate_hotel_software_v30.model.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", expression = "java(com.example.ultimate_hotel_software_v30.enums.Role.valueOf(request.getName()))")
    RoleEntity toRoleEntity(RoleDTORequest request);

    @Mapping(target = "name", source = "role")
    RoleDTOResponse toRoleDTOResponse(RoleEntity entity);

}