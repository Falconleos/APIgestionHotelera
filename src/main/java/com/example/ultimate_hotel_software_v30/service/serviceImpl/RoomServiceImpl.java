package com.example.ultimate_hotel_software_v30.service.serviceImpl;

import com.example.ultimate_hotel_software_v30.dto.request.RoomDTORequest;
import com.example.ultimate_hotel_software_v30.dto.request.RoomUpdateDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.RoomDTOResponse;
import com.example.ultimate_hotel_software_v30.enums.RoomState;
import com.example.ultimate_hotel_software_v30.exceptions.DuplicatedRoomException;
import com.example.ultimate_hotel_software_v30.exceptions.RoomNotFoundException;
import com.example.ultimate_hotel_software_v30.exceptions.RoomUnderMaintenanceException;
import com.example.ultimate_hotel_software_v30.mapper.RoomMapper;
import com.example.ultimate_hotel_software_v30.model.RoomEntity;
import com.example.ultimate_hotel_software_v30.model.RoomTypeEntity;
import com.example.ultimate_hotel_software_v30.repository.RoomRepository;
import com.example.ultimate_hotel_software_v30.repository.RoomTypeRepository;
import com.example.ultimate_hotel_software_v30.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomMapper roomMapper;

    @Override
    @Transactional(readOnly = true)
    public RoomEntity findEntityById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException("Room not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public RoomDTOResponse findById(Long id) {
        RoomEntity room = findEntityById(id);
        return roomMapper.toRoomDTOResponse(room);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomEntity> findAll() {
        return roomRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDTOResponse> getAll() {
        return roomRepository.findAll().stream()
                .map(roomMapper::toRoomDTOResponse)
                .toList();
    }

    @Override
    @Transactional
    public RoomDTOResponse save(RoomDTORequest dto) {
        // 1. Validación de número único
        if (roomRepository.findByNumber(dto.getNumber()).isPresent()) {
            throw new DuplicatedRoomException("A room with number " + dto.getNumber() + " already exists.");
        }

        // 2. Buscar y asociar el RoomTypeEntity obligatorio
        RoomTypeEntity roomType = roomTypeRepository.findById(dto.getRoomTypeId())
                .orElseThrow(() -> new RoomNotFoundException("Room Type not found with ID: " + dto.getRoomTypeId()));

        // 3. Mapeo base
        RoomEntity room = roomMapper.toRoomEntity(dto);
        room.setType(roomType);

        // Si el DTO no trae el estado o quieres forzar el que viene:
        if (dto.getState() != null) {
            room.setState(dto.getState());
        } else {
            room.setState(RoomState.AVAILABLE);
        }

        // 4. Procesar imágenes MultipartFile a List<byte[]> si existen
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            List<byte[]> imageBytesList = new ArrayList<>();
            for (MultipartFile file : dto.getImages()) {
                if (!file.isEmpty()) {
                    try {
                        imageBytesList.add(file.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to store image file", e);
                    }
                }
            }
            room.setImages(imageBytesList);
        }

        RoomEntity savedRoom = roomRepository.save(room);
        return roomMapper.toRoomDTOResponse(savedRoom);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        RoomEntity room = findEntityById(id);

        if (room.getState() == RoomState.OCCUPIED) {
            throw new IllegalArgumentException("Cannot delete an occupied room.");
        }

        roomRepository.delete(room);
    }

    @Override
    @Transactional
    public RoomDTOResponse update(Long id, RoomUpdateDTORequest dto) {
        RoomEntity room = findEntityById(id);

        // Si el DTO incluye un cambio de tipo de habitación, lo actualizamos
        if (dto.getRoomTypeId() != null && !dto.getRoomTypeId().equals(room.getType().getId())) {
            RoomTypeEntity newType = roomTypeRepository.findById(dto.getRoomTypeId())
                    .orElseThrow(() -> new RoomNotFoundException("Room Type not found with ID: " + dto.getRoomTypeId()));
            room.setType(newType);
        }

        // Mapeo de actualizaciones básicas (ajustado al nuevo nombre del método del mapper)
        RoomEntity roomUpdateSource = roomMapper.toRoomEntityFromUpdate(dto);

        // Aplicamos campos modificados manualmente o mediante mapper auxiliar si lo prefieres
        if (roomUpdateSource.getNumber() != null) {
            room.setNumber(roomUpdateSource.getNumber());
        }

        RoomEntity updatedRoom = roomRepository.save(room);
        return roomMapper.toRoomDTOResponse(updatedRoom);
    }

    @Override
    @Transactional
    public void updateRoom(RoomEntity roomEntity) {
        roomRepository.save(roomEntity);
    }

    @Override
    @Transactional
    public RoomDTOResponse doMaintenance(Long id) {
        RoomEntity room = findEntityById(id);

        if (room.getState() == RoomState.MAINTENANCE) {
            throw new RoomUnderMaintenanceException("The room is already under maintenance.");
        }

        room.setState(RoomState.MAINTENANCE);
        RoomEntity updatedRoom = roomRepository.save(room);

        return roomMapper.toRoomDTOResponse(updatedRoom);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDTOResponse> availableRooms() {
        return roomRepository.findByState(RoomState.AVAILABLE)
                .stream()
                .map(roomMapper::toRoomDTOResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomEntity> findByRoomState(RoomState roomState) {
        return roomRepository.findByState(roomState);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer roomCount() {
        return (int) roomRepository.count();
    }

    @Override
    @Transactional
    public RoomDTOResponse addImages(Long id, List<MultipartFile> images) {
        RoomEntity room = findEntityById(id);

        if (images != null && !images.isEmpty()) {
            for (MultipartFile file : images) {
                if (!file.isEmpty()) {
                    try {
                        room.getImages().add(file.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to store image file", e);
                    }
                }
            }
        }

        RoomEntity updatedRoom = roomRepository.save(room);
        return roomMapper.toRoomDTOResponse(updatedRoom);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getImage(Long roomId, int imageIndex) {
        RoomEntity room = findEntityById(roomId);

        if (room.getImages() == null || room.getImages().isEmpty()) {
            throw new RuntimeException("No images found for room ID: " + roomId);
        }

        if (imageIndex < 0 || imageIndex >= room.getImages().size()) {
            throw new RuntimeException("Image index out of bounds: " + imageIndex);
        }

        return room.getImages().get(imageIndex);
    }

}