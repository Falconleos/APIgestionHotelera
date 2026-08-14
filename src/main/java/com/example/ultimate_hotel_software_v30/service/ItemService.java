package com.example.ultimate_hotel_software_v30.service;

import com.example.ultimate_hotel_software_v30.dto.request.ItemDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.ItemDTOResponse;

import java.util.List;

public interface ItemService {
    List<ItemDTOResponse> getAllItems();
    ItemDTOResponse findById(Long id);
    ItemDTOResponse createItem(ItemDTORequest request);
    ItemDTOResponse updateItem(Long id, ItemDTORequest request);
    void deleteItem(Long id);
}