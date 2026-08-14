package com.example.ultimate_hotel_software_v30.service.serviceImpl;
import com.example.ultimate_hotel_software_v30.model.RoomEntity;
import com.example.ultimate_hotel_software_v30.model.RoomTypeEntity;
import com.example.ultimate_hotel_software_v30.repository.RoomTypeRepository;
import com.example.ultimate_hotel_software_v30.service.RoomTypeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomTypeServiceImpl implements RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RoomTypeEntity> getAllRoomTypes() {
        return roomTypeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public RoomTypeEntity getRoomTypeById(Long id) {
        return roomTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room type not found with ID: " + id));
    }

    @Override
    @Transactional
    public RoomTypeEntity createRoomType(RoomTypeEntity roomType) {
        if (roomTypeRepository.existsByName(roomType.getName())) {
            throw new IllegalArgumentException("A room type with the name '" + roomType.getName() + "' already exists.");
        }
        return roomTypeRepository.save(roomType);
    }

    @Override
    @Transactional
    public RoomTypeEntity updateRoomType(Long id, RoomTypeEntity roomTypeDetails) {
        RoomTypeEntity existingType = getRoomTypeById(id);

        if (!existingType.getName().equalsIgnoreCase(roomTypeDetails.getName()) &&
                roomTypeRepository.existsByName(roomTypeDetails.getName())) {
            throw new IllegalArgumentException("A room type with the name '" + roomTypeDetails.getName() + "' already exists.");
        }

        existingType.setName(roomTypeDetails.getName());
        existingType.setCapacity(roomTypeDetails.getCapacity());
        existingType.setDescription(roomTypeDetails.getDescription());
        existingType.setPricePerNight(roomTypeDetails.getPricePerNight());

        return roomTypeRepository.save(existingType);
    }

    @Override
    @Transactional
    public void deleteRoomType(Long id) {
        // 1. Recuperamos el RoomType que se quiere eliminar
        RoomTypeEntity roomTypeToDelete = roomTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot delete. Room type not found with ID: " + id));

        // 2. Seguridad: No permitir borrar el tipo comodín "Basic"
        if ("Basic".equalsIgnoreCase(roomTypeToDelete.getName())) {
            throw new IllegalArgumentException("The default 'Basic' room type cannot be deleted as it is used for fallback.");
        }

        // 3. Recuperamos el tipo "Basic" para reasignar las habitaciones huérfanas
        RoomTypeEntity basicRoomType = roomTypeRepository.findByName("Basic")
                .orElseThrow(() -> new EntityNotFoundException("Critical error: Default room type 'Basic' not found in database."));

        // 4. Reasignamos las habitaciones asociadas al tipo "Basic"
        if (roomTypeToDelete.getRooms() != null && !roomTypeToDelete.getRooms().isEmpty()) {
            for (RoomEntity room : roomTypeToDelete.getRooms()) {
                room.setType(basicRoomType); // En vez de null, le asignamos el tipo Basic
            }
            // Limpiamos la lista para desasociarlas de la entidad que se va a eliminar
            roomTypeToDelete.getRooms().clear();
        }

        // 5. Procedemos a eliminar la categoría de forma segura
        roomTypeRepository.delete(roomTypeToDelete);
    }
}
