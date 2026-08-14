package com.example.ultimate_hotel_software_v30.service.serviceImpl;

import com.example.ultimate_hotel_software_v30.dto.request.RoomAttentionDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.RoomAttentionDTOResponse;
import com.example.ultimate_hotel_software_v30.mapper.RoomAttentionMapper;
import com.example.ultimate_hotel_software_v30.model.BookingEntity;
import com.example.ultimate_hotel_software_v30.model.ItemEntity;
import com.example.ultimate_hotel_software_v30.model.RoomAttentionEntity;
import com.example.ultimate_hotel_software_v30.model.UserEntity;
import com.example.ultimate_hotel_software_v30.repository.BookingRepository;
import com.example.ultimate_hotel_software_v30.repository.ItemRepository;
import com.example.ultimate_hotel_software_v30.repository.RoomAttentionRepository;
import com.example.ultimate_hotel_software_v30.repository.UserRepository;
import com.example.ultimate_hotel_software_v30.service.AccountService;
import com.example.ultimate_hotel_software_v30.service.RoomAttentionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomAttentionServiceImpl implements RoomAttentionService {

    private final RoomAttentionRepository roomAttentionRepository;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final AccountService accountService;
    private final RoomAttentionMapper roomAttentionMapper; // Inyectamos el MapStruct

    @Override
    @Transactional
    public RoomAttentionDTOResponse addAttention(RoomAttentionDTORequest request) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity employee = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Authenticated employee not found"));

        BookingEntity bookingEntity = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        ItemEntity item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new RuntimeException("Item or service not found"));

        // Lógica de stock: Si NO es un servicio, descontamos stock y validamos disponibilidad
        if (!Boolean.TRUE.equals(item.getIsService())) {
            if (item.getQuantity() < request.getQuantity()) {
                throw new IllegalArgumentException("Stock insuficiente para el ítem: " + item.getDescription() + ". Stock disponible: " + item.getQuantity());
            }
            item.setQuantity(item.getQuantity() - request.getQuantity());
            itemRepository.save(item);
        }

        // Construcción de la entidad
        RoomAttentionEntity attention = RoomAttentionEntity.builder()
                .bookingEntity(bookingEntity)
                .item(item)
                .quantity(request.getQuantity())
                .unitPrice(item.getUnitPrice())
                .userEntity(employee)
                .build();

        RoomAttentionEntity saved = roomAttentionRepository.save(attention);

        // Sumamos el subtotal específicamente como servicio/consumo extra a la cuenta
        double totalCharge = saved.getSubtotal();
        accountService.addChargeToAccount(bookingEntity.getId(), totalCharge);

        // Retornamos usando el mapper
        return roomAttentionMapper.toRoomAttentionDTOResponse(saved);
    }

    @Override
    @Transactional
    public void removeAttention(Long id) {
        RoomAttentionEntity attention = roomAttentionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room attention not found"));

        ItemEntity item = attention.getItem();

        // Si NO es un servicio, devolvemos la cantidad al stock al eliminar la atención
        if (!Boolean.TRUE.equals(item.getIsService())) {
            item.setQuantity(item.getQuantity() + attention.getQuantity());
            itemRepository.save(item);
        }

        // Restamos el subtotal del consumo eliminado de los servicios de la cuenta
        double totalCharge = attention.getSubtotal();
        accountService.subtractChargeFromAccount(attention.getBookingEntity().getId(), totalCharge);

        roomAttentionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomAttentionDTOResponse> getAttentionsByBookingId(Long bookingId) {
        return roomAttentionRepository.findByBookingEntity_Id(bookingId).stream()
                .map(roomAttentionMapper::toRoomAttentionDTOResponse)
                .collect(Collectors.toList());
    }
}