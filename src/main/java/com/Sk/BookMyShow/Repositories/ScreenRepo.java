package com.Sk.BookMyShow.Repositories;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.Sk.BookMyShow.Entity.Screen;

@Repository 
public interface ScreenRepo extends MongoRepository<Screen,ObjectId>{
    
    boolean existsByTheatreIdAndName(String theatreId,String name);
    Optional<List<Screen>> findByTheatreId(String theatreId);
} 