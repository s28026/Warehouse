package edu.pja.mas.warehouse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"item_id", "delivery_id"})
})
public class WarehouseDeliveryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private StorageItem storageItem;

    @ManyToOne
    @JoinColumn(name = "delivery_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private WarehouseDelivery delivery;

    @NotNull
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    public double getTotalWeight() {
        return storageItem.getWeight() * quantity;
    }
}