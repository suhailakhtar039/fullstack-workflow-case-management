package com.caseflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest{
    @NotBlank
    String username;

    @NotBlank
    @Size(min = 6)
    String password;

    @NotBlank
    String role;
}
