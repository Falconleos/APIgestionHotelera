package com.example.ultimate_hotel_software_v30.mapper;

import com.example.ultimate_hotel_software_v30.dto.request.BookingCancellationDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.BookingCancellationDTOResponse;
import com.example.ultimate_hotel_software_v30.model.BookingCancellationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingCancellationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booking", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "cancellationDate", ignore = true)
    BookingCancellationEntity toBookingCancellationEntity(BookingCancellationDTORequest request);

    @Mapping(target = "bookingId", source = "booking.id")
    @Mapping(target = "nombreApellido", expression = "java(entity.getEmployee().getUserEntity().getName() + \" \" + entity.getEmployee().getUserEntity().getSurname())")
    @Mapping(target = "employeeUsername", source = "employee.userEntity.username")
    BookingCancellationDTOResponse toBookingCancellationDTOResponse(BookingCancellationEntity entity);

}