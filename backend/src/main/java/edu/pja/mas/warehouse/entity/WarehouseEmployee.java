package edu.pja.mas.warehouse.entity;

import edu.pja.mas.warehouse.enums.WarehouseEmployeeType;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @OneToOne
    @JoinColumn(name = "employee_pesel", unique = true, nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Warehouse warehouse;

    @OneToMany(mappedBy = "assignedWarehouseEmployee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<WarehouseDelivery> handledDeliveries = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private WarehouseEmployeeType type;

    // The following two fields represent the dynamic state of the WarehouseEmployee entity.
    @OneToOne(mappedBy = "warehouseEmployee")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private WarehouseOperator warehouseOperator;

    @OneToOne(mappedBy = "warehouseEmployee")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private WarehouseWorker warehouseWorker;

    // This method is used to validate the dynamic state of the WarehouseEmployee entity.
    public void validateDynamic() {
        if (warehouseOperator == null && warehouseWorker == null)
            throw new IllegalStateException("WarehouseEmployee must be either a WarehouseOperator or a WarehouseWorker");
        if (warehouseOperator != null && warehouseWorker != null)
            throw new IllegalStateException("WarehouseEmployee cannot be both a WarehouseOperator and a WarehouseWorker");
    }

    public boolean isWarehouseOperator() {
        return type == WarehouseEmployeeType.OPERATOR;
    }

    public boolean isWarehouseWorker() {
        return type == WarehouseEmployeeType.WORKER;
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
