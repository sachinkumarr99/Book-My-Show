package com.Sk.BookMyShow.Repositories;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.Sk.BookMyShow.Entity.User;

@Repository 
public interface UserRepo extends MongoRepository<User,ObjectId>{
    
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}
