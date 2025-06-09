package edu.pja.mas.warehouse.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseEmployee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_pesel")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Warehouse warehouse;

    @OneToMany(mappedBy = "assignedWarehouseEmployee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<WarehouseDelivery> handledDeliveries = new HashSet<>();

    // The following two fields represent the dynamic state of the WarehouseEmployee entity.
    @OneToOne(mappedBy = "warehouseEmployee", cascade = CascadeType.ALL, orphanRemoval = true)
    private WarehouseOperator warehouseOperator;

    @OneToOne(mappedBy = "warehouseEmployee", cascade = CascadeType.ALL, orphanRemoval = true)
    private WarehouseWorker warehouseWorker;

    // This method is used to validate the dynamic state of the WarehouseEmployee entity.
    public void validateDynamic() {
        if (warehouseOperator == null && warehouseWorker == null)
            throw new IllegalStateException("WarehouseEmployee must be either a WarehouseOperator or a WarehouseWorker");
        if (warehouseOperator != null && warehouseWorker != null)
            throw new IllegalStateException("WarehouseEmployee cannot be both a WarehouseOperator and a WarehouseWorker");
    }

    public boolean isWarehouseOperator() {
        return warehouseOperator != null;
    }

    public boolean isWarehouseWorker() {
        return warehouseWorker != null;
    }

    public boolean canUnloadItem(WarehouseDeliveryItem item) {
        if (!employee.isWarehouseEmployee())
            throw new IllegalStateException("Employee is not a warehouse employee");

        if (isWarehouseWorker()) {
            return item.getTotalWeight() <= warehouseWorker.getCapacity();
        } else if (isWarehouseOperator()) {
            return true; // WarehouseOperator can unload all kinds of items
        } else {
            throw new IllegalStateException("WarehouseEmployee must be either a WarehouseOperator or a WarehouseWorker");
        }
    }
}
