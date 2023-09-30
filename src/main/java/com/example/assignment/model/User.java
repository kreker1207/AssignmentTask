package com.example.assignment.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@FieldNameConstants
public class User {
    private Long id;
    @Email
    @NotNull
    private String email;
    @NotNull
    private String firstName;
    private String lastName;
    @NotNull
    private LocalDate birthDate;
    private String address;
    private String phone;

    public UserDTO toUserDto(){
        return new UserDTO(email,firstName,lastName,birthDate,address,phone);
    }

}
