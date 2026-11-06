package com.Sk.BookMyShow.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Sk.BookMyShow.DTOs.CreateScreenRequestDto;
import com.Sk.BookMyShow.Entity.Response;
import com.Sk.BookMyShow.Services.ScreenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
// @RequestMapping ("/theatre")
@RequiredArgsConstructor 
public class ScreenController {

    private final ScreenService screenService;


    

      // ----------------------------------screens ----------------------------


     // create screen

     @PostMapping ("theatre-owner/theatre/{theatreId}/screen")
     public ResponseEntity<?> createScreen(@PathVariable String theatreId,
                                           @Valid @RequestBody CreateScreenRequestDto screenRequestDto){

      Map<String,Object> data = new HashMap<>(Map.of("data",screenService.createScreen(screenRequestDto, theatreId)));
      return Response.buildSuccessResponse("Screen Created Successfully", HttpStatus.CREATED, data);

     }

     //update screen

     @PutMapping ("theatre-owner/theatre/{theatreId}/screen/{screenId}")
      public ResponseEntity<?> updateScreen(@PathVariable String theatreId,
                                            @PathVariable String screenId,
                                            @Valid @RequestBody CreateScreenRequestDto screenRequestDto){

      Map<String,Object> data = new HashMap<>(Map.of("data",screenService.updateScreen(screenRequestDto,theatreId,screenId)));
      return Response.buildSuccessResponse("Screen updated Successfully", HttpStatus.OK, data);

     }

     // get screen belongs to particular Theatre
     @GetMapping ("theatre/{theatreId}/screen")
     public ResponseEntity<?> getScreens(@PathVariable String theatreId){

      Map<String,Object> data = new HashMap<>(Map.of("data",screenService.getScreens(theatreId)));
      return Response.buildSuccessResponse("Screens Fetched Successfully", HttpStatus.OK, data);
     }

      
     // get screens
     @GetMapping ("/screens")
     public ResponseEntity<?> getScreens(){

      Map<String,Object> data = new HashMap<>(Map.of("data",screenService.getScreens()));
      return Response.buildSuccessResponse("Screens Fetched Successfully", HttpStatus.OK, data);
     }

     @DeleteMapping("/theatre-owner/theatre/{theatreId}/screen/{screenId}/deactivate")
     public ResponseEntity<?> dactivateScreen(@PathVariable String theatreId,
                                            @PathVariable String screenId){

        screenService.deactivateScreen(screenId, theatreId);                                     
        return Response.buildSuccessResponse("Screen Deactivated Successfully", HttpStatus.OK,null);

    }

         @DeleteMapping("/theatre-owner/theatre/{theatreId}/screen/{screenId}")
     public ResponseEntity<?> deleteScreen(@PathVariable String theatreId,
                                            @PathVariable String screenId){

        screenService.deleteScreen(screenId, theatreId);                                     
        return Response.buildSuccessResponse("Screen Deleted Successfully", HttpStatus.OK,null);

    }

}
