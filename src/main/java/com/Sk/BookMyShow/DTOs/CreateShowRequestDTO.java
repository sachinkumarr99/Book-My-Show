package com.Sk.BookMyShow.DTOs;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Currency;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder 
@AllArgsConstructor 
@NoArgsConstructor 
public class CreateShowRequestDTO {

    // @NotBlank(message = "Theatre ID is required")
    // private String theatreId;

    // @NotBlank(message = "Screen ID is required")
    // private String screenId;

    @NotBlank(message = "Show/Movie Name is required")
    private String showName;

    @NotNull(message = "Show date is required")
    @FutureOrPresent(message = "Show date cannot be in the past")
    private LocalDate showDate;

    @NotNull(message = "Show start time is required")
    @FutureOrPresent(message = "Show Start time cannot be in the past") 
    private LocalTime startTime;

    @NotNull(message = "Show end time is required")
    @FutureOrPresent(message = "Show End time cannot be in the past") 
    private LocalTime endTime;

    @NotBlank(message = "Language is required")
    @Size(max = 30, message = "Language cannot exceed 30 characters")
    private String language;

    @NotNull(message = "Base price is required")
    @Positive(message = "Base price must be greater than zero")
    private Double basePrice;

    @NotBlank
    private String currencyCode;    // INR,USD , etc 
}
