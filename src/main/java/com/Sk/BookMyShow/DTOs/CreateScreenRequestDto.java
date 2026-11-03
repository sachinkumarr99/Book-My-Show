package com.Sk.BookMyShow.DTOs;

import com.Sk.BookMyShow.Enums.SeatCreation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data 
public class CreateScreenRequestDto {
    

    @NotBlank(message = "Screen name is required")
    private String name;

    @NotNull(message = "Total rows are required")
    @Min(value = 1, message = "At least 1 row is required")
    @Max(value = 50, message = "Maximum 50 rows allowed")
    private Integer totalRows;

    @NotNull(message = "Total columns are required")
    @Min(value = 1, message = "At least 1 column is required")
    @Max(value = 50, message = "Maximum 50 columns allowed")
    private Integer totalColumns;

    @NotNull(message = "Please Define your seat creation pattern (DEFAULT,CUSTOMIZE)")
    private SeatCreation seatCreation ;
}
