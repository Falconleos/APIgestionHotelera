package com.example.ultimate_hotel_software_v30.enums;

public enum AccountState {
    OPEN,      // Cuenta abierta con pagos pendientes o parciales
    PAID,      // Cuenta totalmente pagada
    CANCELLED, // Reserva cancelada sin devolución (ej. penalización total)
    REFUNDED   // Reserva cancelada y se emitió Nota de Crédito
}