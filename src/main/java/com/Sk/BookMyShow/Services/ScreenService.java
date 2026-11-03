package com.Sk.BookMyShow.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.Sk.BookMyShow.DTOs.CreateScreenRequestDto;
import com.Sk.BookMyShow.Entity.Screen;
import com.Sk.BookMyShow.Entity.Seat;
import com.Sk.BookMyShow.Entity.Theatre;
import com.Sk.BookMyShow.Entity.Deleted_Entity.DeletedScreen;
import com.Sk.BookMyShow.Entity.Deleted_Entity.DeletedSeat;
import com.Sk.BookMyShow.Enums.SeatCreation;
import com.Sk.BookMyShow.Exceptions.DuplicateResourceException;
import com.Sk.BookMyShow.Exceptions.ResourceNotFoundException;
import com.Sk.BookMyShow.Mapper.ScreenMapper;
import com.Sk.BookMyShow.Repositories.ScreenRepo;
import com.Sk.BookMyShow.Repositories.TheatreRepo;
import com.Sk.BookMyShow.Repositories.Deleted_data_Repo.DeletedScreenRepo;
import com.Sk.BookMyShow.Repositories.Deleted_data_Repo.DeletedSeatRepo;
import com.Sk.BookMyShow.Utils.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScreenService {

    private final ScreenRepo screenRepo;
    private final TheatreRepo theatreRepo;
    private final ScreenMapper screenMapper;
    private final SeatService seatService;
    private final DeletedSeatRepo deletedSeatRepo;
    private final DeletedScreenRepo deletedScreenRepo;

    private String checkTheatreValid(String theatreId) {

        Theatre theatre = theatreRepo.findById(new ObjectId(theatreId))
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not Exist"));
        return theatre.getOwnerId();

    }

    

    void validateScreenAndTheatre(String screenId, String theatreId) {

        // 1) Screen exist karti hai?
        Screen screen = screenRepo.findById(new ObjectId(screenId))
                .orElseThrow(() -> new ResourceNotFoundException("Invalid Screen Id"));

        // 2) Screen isi theatre ki hai?
        if (!screen.getTheatreId().equals(theatreId)) {
            throw new ResourceNotFoundException("Screen does not belong to this theatre");
        }

        // 3) Theatre exist karta hai?
        Theatre theatre = theatreRepo.findById(new ObjectId(theatreId))
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not Exist"));

        // 4) Logged-in user hi owner hai?
        if (!SecurityUtils.getCurrentLoggedUserId().equals(theatre.getOwnerId())) {
            throw new AccessDeniedException("You can't delete seats of someone else's theatre");
        }

    }

    public Map<String, Object> createScreen(CreateScreenRequestDto screenRequestDto, String theatreId) {

        // check theatre is valid or not for that particular user
        String theatreOwnerId = checkTheatreValid(theatreId);

        if (!SecurityUtils.getCurrentLoggedUserId().equals(theatreOwnerId))
            throw new ResourceNotFoundException("This is not your theatre");

        if (screenRepo.existsByTheatreIdAndName(theatreId, screenRequestDto.getName()))
            throw new DuplicateResourceException("Screen already exists");

        Screen screen = screenMapper.toEntity(screenRequestDto, theatreId);
        Map<String, Object> data = new HashMap<>();
        data.put("screen", screenRepo.save(screen));

        List<Seat> seats = new ArrayList<>();

        // seats create (default creation)
        if (screenRequestDto.getSeatCreation() == SeatCreation.DEFAULT) {
            seats = seatService.addRows(0, screenRequestDto.getTotalRows(), 0, screenRequestDto.getTotalColumns(),
                    String.valueOf(screen.getId()));
        }

        // seats create (Customize)
        return Map.of("Seats", seats);

    }

    public Map<String, Object> getScreens(String theatreId) {

        // check theatre exist or not
        checkTheatreValid(theatreId);

        Map<String, Object> map = new HashMap<>();
        map.put("screens", screenRepo.findByTheatreId(theatreId));
        return map;
    }

    public Map<String, Object> getScreens() {

        Map<String, Object> map = new HashMap<>();
        map.put("screens", screenRepo.findAll());
        return map;
    }

    public Map<String, Object> updateScreen(CreateScreenRequestDto screenRequestDto, String theatreId,
            String screenId) {

       validateScreenAndTheatre(screenId, theatreId);
        Screen screen = screenRepo.findById(new ObjectId(screenId))
                .orElseThrow(() -> new ResourceNotFoundException("Screen Not Found"));
        // check duplicate name of Screen
        if (!screen.getName().equals(screenRequestDto.getName())
                && screenRepo.existsByTheatreIdAndName(
                        theatreId, screenRequestDto.getName())) {

            throw new DuplicateResourceException(
                    "Screen already exists in this theatre");
        }

        // update seats ,it will automatically save
        List<Seat> updatedSeats = seatService.updateSeats(screenRequestDto.getTotalRows(),
                screenRequestDto.getTotalColumns(), screen);

        // 5. Update screen fields
        screen.setName(screenRequestDto.getName());
        screen.setTotalRows(screenRequestDto.getTotalRows());
        screen.setTotalColumns(screenRequestDto.getTotalColumns());

        // 6. Save updated screen
        Screen updatedScreen = screenRepo.save(screen);

        return new HashMap<>(Map.of("updatedScreen", updatedScreen, "updated Seats", updatedSeats));

    }

    public void deactivateSeats(String screenId,String theatreId){

        validateScreenAndTheatre(screenId, theatreId);
        seatService.deactivateSeats(theatreId, screenId);

    }
    public void deactivateScreen(String screenId,String theatreId){
        validateScreenAndTheatre(screenId, theatreId);
        Screen screen = screenRepo.findById(new ObjectId(screenId)).orElseThrow(()->new ResourceNotFoundException("Screen Not found"));
        screen.setActive(false);
        deactivateSeats(screenId, theatreId);
        screenRepo.save(screen);

    }

    public void deleteScreen(String screenId,String theatreId){

        validateScreenAndTheatre(screenId, theatreId);

        Screen screen= screenRepo.findById(new ObjectId(screenId)).orElseThrow(()->new ResourceNotFoundException("Screen Not exist"));
        screen.setActive(false);
        // take backup of screen
        DeletedScreen deletedScreen = new DeletedScreen();
        deletedScreen.setOriginalScreenId(screenId);
        deletedScreen.setScreenData(screen);
        deletedScreenRepo.save(deletedScreen);


        deleteSeats(screenId, theatreId);
        screenRepo.deleteById(new ObjectId(screenId));

    }

     public void deleteSeats(String screenId,String theatreId){

        validateScreenAndTheatre(screenId, theatreId);
        // it will automatically take backup
        seatService.deleteSeats( screenId);
        
    }

    public List<Seat> getScreenSeats(String screenId){
        return seatService.getAllSeats(screenId);
    }

}
