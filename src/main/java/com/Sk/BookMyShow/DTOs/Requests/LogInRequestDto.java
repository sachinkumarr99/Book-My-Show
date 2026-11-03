package com.Sk.BookMyShow.DTOs.Requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data 
public class LogInRequestDto {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email")
    private String email;

    private String password;
}
