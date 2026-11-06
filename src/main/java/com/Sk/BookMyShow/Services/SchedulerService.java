package com.Sk.BookMyShow.Services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.Sk.BookMyShow.Entity.Show;
import com.Sk.BookMyShow.Enums.ShowStatus;
import com.Sk.BookMyShow.Repositories.ShowRepo;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class SchedulerService {
    
    private final ShowRepo showRepo;
    
    
    // Every 60 seconds after previous execution completes
    @Scheduled(fixedDelay = 60000)
    public void completeExpiredShows() {

        // Expired shows ko COMPLETED karna
        System.out.println("Scheduler Executed: " + LocalTime.now());
        List<Show> updatedShows = showRepo.findByShowDateAndEndTimeLessThanAndStatus(LocalDate.now(),LocalTime.now(),ShowStatus.SCHEDULED);
        updatedShows.forEach(show->show.setStatus(ShowStatus.COMPLETED));
        System.out.println("Scheduler updated shows : " + LocalTime.now());
        showRepo.saveAll(updatedShows);
        System.out.println("Scheduler save Executed: " + LocalTime.now());
        
    }

    // Every day at 2 AM
    @Scheduled(cron = "0 0 2 * * *")
    public void deleteCompletedShows() {

        // Completed shows ko delete karna
        showRepo.deleteByStatus(ShowStatus.COMPLETED);
    }
}
