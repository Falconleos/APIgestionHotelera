package com.example.ultimate_hotel_software_v30.mapper;

import com.example.ultimate_hotel_software_v30.dto.request.BookingDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.BookingDTOResponse;
import com.example.ultimate_hotel_software_v30.model.BookingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "userEntity", ignore = true)
    @Mapping(target = "qrBooking", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "employeeBookingEntity", ignore = true)
    @Mapping(target = "employeeCheckInEntity", ignore = true)
    @Mapping(target = "roomEntity", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "cancellation", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    BookingEntity toBookingEntity(BookingDTORequest request);

    @Mapping(target = "name", source = "userEntity.name")
    @Mapping(target = "surname", source = "userEntity.surname")
    @Mapping(target = "username", source = "userEntity.username")
    @Mapping(target = "employeeBookingUsername", source = "employeeBookingEntity.userEntity.username")
    @Mapping(target = "employeeCheckInUsername", source = "employeeCheckInEntity.userEntity.username")
    @Mapping(target = "roomNumber", source = "roomEntity.number")
    BookingDTOResponse toBookingDTOResponse(BookingEntity entity);

}