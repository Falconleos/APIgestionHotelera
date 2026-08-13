package com.example.ultimate_hotel_software_v30.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTORequest {

    @NotNull(message = "account must not be null")
    private Long accountId;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
    private Double amount;

    private String paymentMethod; // Ej: CASH, CREDIT_CARD, TRANSFER

    private String transactionReference; // Número de comprobante o voucher

}
