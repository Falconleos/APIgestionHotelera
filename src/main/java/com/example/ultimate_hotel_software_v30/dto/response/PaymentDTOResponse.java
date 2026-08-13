package com.example.ultimate_hotel_software_v30.dto.response;

import com.example.ultimate_hotel_software_v30.enums.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de respuesta con el detalle del pago registrado en el sistema")
public class PaymentDTOResponse {

    @Schema(description = "Identificador único del pago", example = "1")
    private Long id;

    @Schema(description = "Monto abonado", example = "45000.50")
    private Double amount;

    @Schema(description = "Fecha y hora exacta en la que se realizó el pago", example = "2026-08-13T14:30:00")
    private LocalDateTime paymentDate;

    @Schema(description = "Método o medio de pago utilizado", example = "CREDIT_CARD")
    private PaymentMethod paymentMethod;

    @Schema(description = "Número de comprobante, voucher o referencia de la transacción", example = "VOUCHER-987654")
    private String transactionReference;

    @Schema(description = "Identificador de la cuenta o estado de cuenta asociado (opcional)", example = "3")
    private Long accountId;

    @Schema(description = "Identificador del usuario o empleado que registró el pago", example = "15")
    private Long userId;

    @Schema(description = "Nombre de usuario del empleado que registró el pago", example = "jperez")
    private String username;

}