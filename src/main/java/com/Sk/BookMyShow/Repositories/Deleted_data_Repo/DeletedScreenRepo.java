package com.Sk.BookMyShow.Repositories.Deleted_data_Repo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.Sk.BookMyShow.Entity.Deleted_Entity.DeletedScreen;

@Repository 
public interface DeletedScreenRepo extends MongoRepository<DeletedScreen,ObjectId>{
    
}
