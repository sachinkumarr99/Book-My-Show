package com.Sk.BookMyShow.Repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.Sk.BookMyShow.Entity.Theatre;

@Repository 
public interface TheatreRepo extends MongoRepository<Theatre,ObjectId>{
    boolean existsByNameAndCityAndStateAndPincode(String name,String city,String state,String pincode);
}
