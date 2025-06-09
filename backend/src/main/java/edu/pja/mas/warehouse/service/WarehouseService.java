package edu.pja.mas.warehouse.service;

import edu.pja.mas.warehouse.dto.WarehousePostDTO;
import edu.pja.mas.warehouse.entity.Warehouse;
import edu.pja.mas.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;

    public Warehouse findById(Long warehouseId) {
        return warehouseRepository.findById(warehouseId).orElse(null);
    }

    public Warehouse findById(Long warehouseId, boolean withEmployees) {
        Warehouse warehouse = findById(warehouseId);
        if (withEmployees)
            Hibernate.initialize(warehouse.getEmployees());
        return warehouse;
    }

    public List<Warehouse> findAll() {
        return warehouseRepository.findAll();
    }

    public Warehouse save(WarehousePostDTO dto) {
        Warehouse warehouse = Warehouse.builder()
                .location(dto.location())
                .build();

        return warehouseRepository.save(warehouse);
    }
}
