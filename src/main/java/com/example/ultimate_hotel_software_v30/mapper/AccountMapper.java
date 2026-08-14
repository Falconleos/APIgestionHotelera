package com.example.ultimate_hotel_software_v30.mapper;

import com.example.ultimate_hotel_software_v30.dto.response.AccountDTOResponse;
import com.example.ultimate_hotel_software_v30.model.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PaymentMapper.class})
public interface AccountMapper {

    @Mapping(target = "bookingId", source = "bookingEntity.id")
    AccountDTOResponse toAccountDTOResponse(AccountEntity entity);

}