package com.example.ultimate_hotel_software_v30.service;

import com.example.ultimate_hotel_software_v30.model.RoomTypeEntity;

import java.util.List;

public interface RoomTypeService {
    List<RoomTypeEntity> getAllRoomTypes();
    RoomTypeEntity getRoomTypeById(Long id);
    RoomTypeEntity createRoomType(RoomTypeEntity roomType);
    RoomTypeEntity updateRoomType(Long id, RoomTypeEntity roomTypeDetails);
    void deleteRoomType(Long id);
}
