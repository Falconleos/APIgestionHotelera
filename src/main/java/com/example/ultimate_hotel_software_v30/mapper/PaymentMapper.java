package com.example.ultimate_hotel_software_v30.mapper;

import com.example.ultimate_hotel_software_v30.dto.request.PaymentDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.PaymentDTOResponse;
import com.example.ultimate_hotel_software_v30.model.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "paymentDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "paymentMethod", expression = "java(request.getPaymentMethod() != null ? com.example.ultimate_hotel_software_v30.enums.PaymentMethod.valueOf(request.getPaymentMethod()) : null)")
    @Mapping(target = "accountEntity", ignore = true)
    @Mapping(target = "userEntity", ignore = true)
    PaymentEntity toPaymentEntity(PaymentDTORequest request);

    @Mapping(target = "accountId", source = "accountEntity.id")
    @Mapping(target = "userId", source = "userEntity.id")
    @Mapping(target = "username", source = "userEntity.username")
    PaymentDTOResponse toPaymentDTOResponse(PaymentEntity entity);

}