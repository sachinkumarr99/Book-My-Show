package com.Sk.BookMyShow.Entity;

import java.time.Instant;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.Sk.BookMyShow.Enums.Role;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Document (collection = "users")
@Getter 
@Setter 
@Builder 
public class User {
    @Id 
    private ObjectId id;
    private String name;
    @Indexed (unique = true)
    private String email;
    private String password; 
    @CreatedDate 
    private Instant createdAt;
    private Role role;

}
