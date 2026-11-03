package com.Sk.BookMyShow.Entity;

import java.time.Instant;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder 
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class Screen {

     @Id
    private ObjectId id;

    // Ye screen kis theatre ki hai
    private String theatreId;

    // Example: Screen 1, Screen 2
    @Indexed (unique = true)
    private String name;

    // Screen ka layout
    private int totalRows;
    private int totalColumns;

    // Screen active hai ya nahi
    @Builder.Default
    private boolean isActive = true;

    @CreatedDate
    private Instant createdAt;
    
}
