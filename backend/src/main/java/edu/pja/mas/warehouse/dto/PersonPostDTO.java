package edu.pja.mas.warehouse.dto;

import java.time.LocalDate;


public record PersonPostDTO(
        String pesel,
        String firstName,
        String lastName,
        LocalDate dateOfBirth
) {
}
