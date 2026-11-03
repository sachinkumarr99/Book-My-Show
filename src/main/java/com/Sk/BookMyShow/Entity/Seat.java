package com.Sk.BookMyShow.Entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.Sk.BookMyShow.Enums.SeatStatus;
import com.Sk.BookMyShow.Enums.SeatType;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "seats")
@Builder 
@AllArgsConstructor
@CompoundIndex(
    name = "screen_seat_label_unique",
    def = "{'screenId': 1, 'seatLabel': 1}",
    unique = true
)
public class Seat {

    @Id
    private ObjectId id;

    // Screen to which this physical seat belongs
    @Field("screenId")
    private String screenId;

    // Example: 00,01,10,11
    private String seatLabel;

    // Example: Row 1,Row 2
    private String rowLabel;

    // Seat number within its row: 1, 2, 3...
    private int seatNumber;

    // Position in the complete screen layout
    private int rowIndex;
    private int columnIndex;

    // Regular, Premium, Recliner, etc.
    private SeatType seatType;

    // Permanent physical seat status
    private SeatStatus status;

    // Audit fields
    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
