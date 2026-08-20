package com.example.ultimate_hotel_software_v30.mapper;

import com.example.ultimate_hotel_software_v30.dto.response.AccountDTOResponse;
import com.example.ultimate_hotel_software_v30.model.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PaymentMapper.class})
public interface AccountMapper {

    @Mapping(target = "bookingId", source = "bookingEntity.id")
    @Mapping(target = "checkInDate", source = "bookingEntity.checkIn")
    @Mapping(target = "checkOutDate", source = "bookingEntity.checkOut")
    @Mapping(target = "roomNumber", source = "bookingEntity.roomEntity.number")
    @Mapping(target = "user.name", source = "bookingEntity.guestFirstName")
    @Mapping(target = "user.surname", source = "bookingEntity.guestLastName")
    @Mapping(target = "user.dni", source = "bookingEntity.userEntity.dni")
    AccountDTOResponse toAccountDTOResponse(AccountEntity entity);

}