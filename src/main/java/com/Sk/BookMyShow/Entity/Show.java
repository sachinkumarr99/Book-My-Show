package com.Sk.BookMyShow.Entity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Currency;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import com.Sk.BookMyShow.Enums.ShowStatus;

import lombok.Builder;
import lombok.Data;

@Document (collection = "shows")
@Data 
@Builder 
@CompoundIndexes ({
    @CompoundIndex(
    name = "theatre_date_status_idx",
    def = "{'theatreId': 1, 'showDate': 1, 'status': 1, 'startTime': 1}"
    ),
    @CompoundIndex(
    name = "screen_date_time_idx",
    def = "{'screenId': 1, 'showDate': 1, 'startTime': 1}",
    unique = true
    ),
    @CompoundIndex(
    name = "status_showDate_idx",
    def = "{'status': 1, 'showDate': 1}"
)
})
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

    private String currencyCode;

    
    @Builder.Default
    private ShowStatus status = ShowStatus.SCHEDULED;

    @CreatedDate 
    private Instant createdAt;
}