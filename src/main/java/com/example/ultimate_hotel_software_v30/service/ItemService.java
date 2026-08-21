package com.example.ultimate_hotel_software_v30.service;

import com.example.ultimate_hotel_software_v30.dto.request.ItemDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.ItemDTOResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {
    List<ItemDTOResponse> getAllItems();
    ItemDTOResponse findById(Long id);
    ItemDTOResponse createItem(ItemDTORequest request, MultipartFile file);
    ItemDTOResponse updateItem(Long id, ItemDTORequest request, MultipartFile file);
    void deleteItem(Long id);
    // Añade esta firma a tu interfaz
    byte[] getItemImage(Long id);
}