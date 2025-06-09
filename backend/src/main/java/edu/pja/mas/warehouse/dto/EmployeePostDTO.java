package edu.pja.mas.warehouse.dto;

public record EmployeePostDTO(
        PersonPostDTO person,
        String phoneNumber,
        int salary,
        WarehouseEmployeePostDTO warehouse,
        DeliveryDriverPostDTO deliveryDriver
) {
}
