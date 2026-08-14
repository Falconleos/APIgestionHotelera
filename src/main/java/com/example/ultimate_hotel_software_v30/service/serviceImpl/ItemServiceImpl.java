package com.example.ultimate_hotel_software_v30.service.serviceImpl;
import com.example.ultimate_hotel_software_v30.dto.request.ItemDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.ItemDTOResponse;
import com.example.ultimate_hotel_software_v30.mapper.ItemMapper;
import com.example.ultimate_hotel_software_v30.model.ItemEntity;
import com.example.ultimate_hotel_software_v30.repository.ItemRepository;
import com.example.ultimate_hotel_software_v30.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ItemDTOResponse> getAllItems() {
        return itemRepository.findAll().stream()
                .map(itemMapper::toItemDTOResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDTOResponse findById(Long id) {
        ItemEntity entity = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item no encontrado con ID: " + id));
        return itemMapper.toItemDTOResponse(entity);
    }

    @Override
    @Transactional
    public ItemDTOResponse createItem(ItemDTORequest request) {
        ItemEntity entity = itemMapper.toItemEntity(request);

        // Si es servicio, forzamos la cantidad a 1
        if (Boolean.TRUE.equals(request.getIsService())) {
            entity.setQuantity(1);
            entity.setIsService(true);
        } else {
            entity.setIsService(false);
        }

        ItemEntity savedEntity = itemRepository.save(entity);
        return itemMapper.toItemDTOResponse(savedEntity);
    }

    @Override
    @Transactional
    public ItemDTOResponse updateItem(Long id, ItemDTORequest request) {
        ItemEntity existingEntity = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item no encontrado para actualizar con ID: " + id));

        existingEntity.setDescription(request.getDescription());
        existingEntity.setUnitPrice(request.getUnitPrice());

        // Forzar cantidad a 1 si es servicio
        if (Boolean.TRUE.equals(request.getIsService())) {
            existingEntity.setQuantity(1);
            existingEntity.setIsService(true);
        } else {
            existingEntity.setQuantity(request.getQuantity());
            existingEntity.setIsService(false);
        }

        ItemEntity updatedEntity = itemRepository.save(existingEntity);
        return itemMapper.toItemDTOResponse(updatedEntity);
    }

    @Override
    @Transactional
    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar, item no encontrado con ID: " + id);
        }
        itemRepository.deleteById(id);
    }
}