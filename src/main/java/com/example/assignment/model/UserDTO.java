package com.example.assignment.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserDTO {
    @Email
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String address;
    private String phone;
}
