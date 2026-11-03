package com.Sk.BookMyShow.Entity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.Sk.BookMyShow.Enums.ShowStatus;

import lombok.Builder;
import lombok.Data;

@Document (collection = "shows")
@Data 
@Builder 
public class Show {

    @Id 
    private ObjectId id;

    private String theatreId;

    private String screenId;

    private String showName;

    private LocalDate showDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private String language;

    private Double basePrice;

    private ShowStatus status;

    private Instant createdAt;
}