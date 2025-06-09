package edu.pja.mas.warehouse.entity;

import edu.pja.mas.warehouse.enums.WarehouseDeliveryStatus;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseDelivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Builder.Default
    private LocalDateTime registeredAt = LocalDateTime.now();

    @Nullable
    private LocalDateTime confirmedAt;

    @Nullable
    private LocalDateTime deliveredAt;

    @Pattern(
            regexp = "^\\s*[\\p{L} .'-]+\\s*,\\s*\\d+[A-Za-z]?\\s*,\\s*[\\p{L} .'-]+\\s*,\\s*\\d{2}-\\d{3}\\s*$",
            message = "Address must be in format: Street, Number, City, ZIP (e.g. Mickiewicza, 12A, Kraków, 30-059)"
    )
    @Nullable
    private String pickupAddress;

    @NotNull
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private WarehouseDeliveryStatus status = WarehouseDeliveryStatus.AWAITING_PICKUP_ADDRESS;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.REMOVE)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<WarehouseDeliveryItem> items = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "assigned_driver_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private DeliveryDriver assignedDriver;

    @ManyToOne
    @JoinColumn(name = "assigned_warehouse_employee_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private WarehouseEmployee assignedWarehouseEmployee;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Warehouse warehouse;

    public int getDuration() {
        if (confirmedAt == null || deliveredAt == null)
            throw new IllegalStateException("Pickup and delivery dates must be set to calculate duration.");

        return (int) Duration.between(confirmedAt, deliveredAt).toMinutes();
    }
}
