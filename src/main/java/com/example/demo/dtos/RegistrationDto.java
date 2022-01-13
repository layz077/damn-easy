package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.Range;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDto {

    @NotNull
    private String name;
    @NotNull
//    @Range(min = 8,message = "Minimum length of the password must be 8")
    private String password;
    private String phoneNumber;
    @NotNull
    private String email;
}
