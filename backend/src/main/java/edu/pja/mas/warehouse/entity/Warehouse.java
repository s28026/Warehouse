package edu.pja.mas.warehouse.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;
import java.util.Map;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Pattern(
            regexp = "^\\s*[\\p{L} .'-]+\\s*,\\s*\\d+[A-Za-z]?\\s*,\\s*[\\p{L} .'-]+\\s*,\\s*\\d{2}-\\d{3}\\s*$",
            message = "Address must be in format: Street, Number, City, ZIP (e.g. Mickiewicza, 12A, Kraków, 30-059)"
    )
    private String location;

    @OneToMany(mappedBy = "warehouse")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<WarehouseEmployee> employees;

    @OneToMany(mappedBy = "warehouse")
    @MapKey(name = "roomNumber")
    private Map<Long, StorageUnit> storageUnits;

    @OneToMany(mappedBy = "warehouse")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<WarehouseDelivery> deliveries;
}
