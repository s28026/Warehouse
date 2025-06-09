package edu.pja.mas.warehouse.dto;

import java.time.LocalDate;
import java.util.List;


public record PersonDTO(
        String pesel,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String fullName
) {
}
