package com.example.ultimate_hotel_software_v30.service;

import com.example.ultimate_hotel_software_v30.dto.request.RoomAttentionDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.RoomAttentionDTOResponse;

import java.util.List;

public interface RoomAttentionService {
    RoomAttentionDTOResponse addAttention(RoomAttentionDTORequest request);
    void removeAttention(Long id);
    List<RoomAttentionDTOResponse> getAttentionsByBookingId(Long bookingId);
}