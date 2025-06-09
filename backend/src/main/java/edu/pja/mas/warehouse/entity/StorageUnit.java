package edu.pja.mas.warehouse.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;


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
    @Column(unique = true)
    private Integer roomNumber;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Warehouse warehouse;

    @OneToOne
    @JoinColumn(name = "warehouse_delivery_item_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private WarehouseDeliveryItem warehouseDeliveryItem;

    @Nullable
    private String nfcTag;

    @Nullable
    private String qrCode;

    public void setNfcTag(String nfcTag) {
        if (nfcTag == null || nfcTag.isBlank())
            throw new IllegalArgumentException("NFC tag cannot be null or blank");

        qrCode = null;
        this.nfcTag = nfcTag.trim();
    }

    public void setQrCode(String qrCode) {
        if (qrCode == null || qrCode.isBlank())
            throw new IllegalArgumentException("QR code cannot be null or blank");

        nfcTag = null;
        this.qrCode = qrCode.trim();
    }

    public StorageItem getStoredItem() {
        return warehouseDeliveryItem.getStorageItem();
    }

    public Integer getItemQuantity() {
        return warehouseDeliveryItem.getQuantity();
    }
}
