package com.Sk.BookMyShow.Repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.Sk.BookMyShow.Entity.Seat;
import com.Sk.BookMyShow.Enums.SeatStatus;

@Repository 
public interface SeatRepo extends MongoRepository<Seat,ObjectId>{
    
    List<Seat> findByScreenId(String screenId);

    @Query (
        "{'screenId': ?0, 'rowIndex' : {$gte:?1,$lt:?2},'columnIndex':{$gte:?3,$lt:?4}  }")
    List<Seat> findSeatsInRange(String screenId,int stRowIdx,int endRow,int stColIdx,int endCol);


    @Query (
        "{'screenId': ?0, 'rowIndex' : {$gte:?1,$lt:?2},'columnIndex':{$gte:?3,$lt:?4} ,'status':'ACTIVE' }")
    List<Seat> findActivateSeats(String screenId,int stRowIdx,int endRow,int stColIdx,int endCol);

    void deleteByScreenId(String screenId);
    


    
}
