package com.Sk.BookMyShow.Entity.Deleted_Entity;

import java.time.Instant;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.Sk.BookMyShow.Entity.Screen;

import lombok.Data;

@Document(collection = "deleted_screen")
@Data 
public class DeletedScreen {
    

    @Id
    private ObjectId id;

    @Indexed (unique = true)
    private String originalScreenId;

    private Screen screenData;

    @CreatedDate 
    private Instant deletedAt;

}
