package edu.pja.mas.warehouse.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StorageUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(100)
    @Max(199)
    @Column(unique = true, nullable = false)
    private Long roomNumber;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @OneToOne
    @JoinColumn(name = "warehouse_delivery_item_id", nullable = false)
    private WarehouseDeliveryItem warehouseDeliveryItem;

    @Nullable
    private String nfcTag;

    @Nullable
    private String qrCode;

    public StorageItem getStoredItem() {
        return warehouseDeliveryItem.getStorageItem();
    }

    public Integer getItemQuantity() {
        return warehouseDeliveryItem.getQuantity();
    }
}
