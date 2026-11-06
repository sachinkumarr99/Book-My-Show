package com.Sk.BookMyShow.Repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.Sk.BookMyShow.Entity.Screen;
import com.Sk.BookMyShow.Entity.Show;
import com.Sk.BookMyShow.Enums.ShowStatus;

@Repository 
public interface ShowRepo extends MongoRepository<Show,ObjectId>{
    
      // Theatre + Date + Status
    List<Show> findByTheatreIdAndShowDateAndStatusOrderByStartTimeAsc(
            String theatreId,
            LocalDate showDate,
            ShowStatus status
    );

    // Screen + Date
    List<Show> findByScreenIdAndShowDateOrderByStartTimeAsc(
            String screenId,
            LocalDate showDate
    );

    // Screen + Date + Start Time
    boolean existsByScreenIdAndShowDateAndStartTime(
            String screenId,
            LocalDate showDate,
            LocalTime startTime
    );

    // check any Show Overlap
    @Query("""
    {
        'screenId': ?0,
        'showDate': ?1,
        'status': ?2,
        'startTime': { '$lt': ?3 },
        'endTime': { '$gt': ?4 }
    }
    """)
    Show overlapShow(
        String screenId,
        LocalDate showDate,
        ShowStatus status,
        LocalTime newEndTime,
        LocalTime newStartTime );

    void deleteByStatus(ShowStatus showStatus);

    List<Show> findByShowDateAndEndTimeLessThanAndStatus(LocalDate showDate,LocalTime endTime,ShowStatus status);


}



