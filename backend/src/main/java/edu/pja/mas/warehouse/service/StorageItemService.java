package edu.pja.mas.warehouse.service;

import edu.pja.mas.warehouse.dto.StorageItemPostDTO;
import edu.pja.mas.warehouse.entity.StorageItem;
import edu.pja.mas.warehouse.repository.StorageItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class StorageItemService {
    private final StorageItemRepository storageItemRepository;

    public StorageItem findById(Long id) {
        return storageItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Storage item not found with id: " + id));
    }

    public StorageItem save(StorageItemPostDTO dto) {
        StorageItem storageItem = StorageItem.builder()
                .itemName(dto.name())
                .barcode(dto.barcode())
                .weight(dto.weight())
                .build();

        return storageItemRepository.save(storageItem);
    }

    public int getTotalItemsCount(Long warehouseId) {
        return (int) storageItemRepository.count();
    }
}
