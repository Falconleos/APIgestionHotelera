package com.example.ultimate_hotel_software_v30.service;

import com.example.ultimate_hotel_software_v30.dto.request.RoomDTORequest;
import com.example.ultimate_hotel_software_v30.dto.request.RoomUpdateDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.RoomDTOResponse;
import com.example.ultimate_hotel_software_v30.enums.RoomState;
import com.example.ultimate_hotel_software_v30.model.RoomEntity;

import java.util.List;

public interface RoomService {
    RoomEntity findEntityById(Long id);
    RoomDTOResponse findById(Long id);

    // 2. Listar habitaciones
    List<RoomEntity> findAll();
    List<RoomDTOResponse> getAll();

    // 3. Crear habitacion
    RoomDTOResponse save(RoomDTORequest dto);

    // 4. Borrar habitacion
    void delete(Long id);

    // 5. Actualizaciones
    RoomDTOResponse update(Long id, RoomUpdateDTORequest dto);
    void updateRoom(RoomEntity roomEntity);
    RoomDTOResponse doMaintenance(Long id);

    // 6. Otras busquedas y listados
    List<RoomDTOResponse> availableRooms();
    List<RoomEntity> findByRoomState(RoomState roomState);

    // 7. Calculos
    Integer roomCount();
}