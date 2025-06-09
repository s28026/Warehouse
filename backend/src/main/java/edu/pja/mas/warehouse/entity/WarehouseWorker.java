package edu.pja.mas.warehouse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseWorker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "warehouse_employee_id")
    private WarehouseEmployee warehouseEmployee;

    @Min(1)
    private Integer capacity;

    public boolean canCarry(int amount) {
        return amount > 0 && amount <= capacity;
    }
}
