package com.example.ultimate_hotel_software_v30.mapper;

import com.example.ultimate_hotel_software_v30.dto.request.CommentDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.CommentDTOResponse;
import com.example.ultimate_hotel_software_v30.model.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userEntity", ignore = true) // Se asigna manualmente en el servicio
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    CommentEntity toCommentEntity(CommentDTORequest request);

    @Mapping(source = "userEntity.name", target = "name")
    @Mapping(source = "userEntity.surname", target = "surname")
    @Mapping(source = "userEntity.username", target = "username")
    CommentDTOResponse toCommentDTOResponse(CommentEntity entity);

}