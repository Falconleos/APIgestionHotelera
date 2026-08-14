package com.example.ultimate_hotel_software_v30.config;

import com.example.ultimate_hotel_software_v30.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingScheduler {

    private final BookingService bookingService;

    @Scheduled(cron = "0 0 4 * * ?")
    public void runNoShowProcessing() {
        log.info(">> Iniciando procesamiento automático diario de ausencias (No-Show)...");
        try {
            bookingService.processNoShowBookings();
            log.info(">> Procesamiento automático de No-Shows finalizado exitosamente.");
        } catch (Exception e) {
            log.error(">> Error al procesar de forma automática los No-Shows: {}", e.getMessage(), e);
        }
    }

}
