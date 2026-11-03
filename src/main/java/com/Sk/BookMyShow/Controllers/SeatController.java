package com.Sk.BookMyShow.Controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Sk.BookMyShow.Entity.Response;
import com.Sk.BookMyShow.Services.ScreenService;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("/theatre")
@RequiredArgsConstructor 
public class SeatController {
    
    private final ScreenService screenService;
    
    @DeleteMapping ("/{theatreId}/owner/screen/{screenId}/seats/deactivate")
     public ResponseEntity<?> deactivateSeats(@PathVariable String theatreId,
                                           @PathVariable String screenId
     ){
            screenService.deactivateSeats(screenId, theatreId);
            return Response.buildSuccessResponse("Seats Deactivated Successfully", HttpStatus.OK,null);
     }

    
     @DeleteMapping ("/{theatreId}/owner/screen/{screenId}/seats")
     public ResponseEntity<?> deleteseats(@PathVariable String theatreId,
                                           @PathVariable String screenId
     ){
            screenService.deleteSeats(screenId, theatreId);
            return Response.buildSuccessResponse("Seats Deleted Successfully", HttpStatus.OK,null);
     }

      @GetMapping("/screen/{screenId}/seats")
     public ResponseEntity<?> getAllSeats(@PathVariable String screenId){
            
            return Response.buildSuccessResponse("Seats Deleted Successfully", HttpStatus.OK,Map.of("Seats",screenService.getScreenSeats(screenId)));
     }

}
