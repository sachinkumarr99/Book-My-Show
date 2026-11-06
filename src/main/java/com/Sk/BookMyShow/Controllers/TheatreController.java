package com.Sk.BookMyShow.Controllers;

import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Sk.BookMyShow.DTOs.CreateScreenRequestDto;
import com.Sk.BookMyShow.DTOs.TheatreDto;
import com.Sk.BookMyShow.Entity.Response;
import com.Sk.BookMyShow.Entity.Screen;
import com.Sk.BookMyShow.Entity.Theatre;
import com.Sk.BookMyShow.Services.ScreenService;
import com.Sk.BookMyShow.Services.TheatreService;
import com.Sk.BookMyShow.Utils.SecurityUtils;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;


@RestController 
@RequiredArgsConstructor 
public class TheatreController {

   // Theatres
    
    private final TheatreService theatreService;

    @PostMapping("/theatre-owner/theatre")
    public ResponseEntity<?> createTheatre(@Valid @RequestBody  TheatreDto theatreDto){
        
        Theatre theatre = theatreService.registerTheatre(theatreDto);
        Map<String,Object> data = new HashMap<>();
        data.put("data",theatre);

        return Response.buildSuccessResponse("Successfully Registered",HttpStatus.CREATED,data);
        
    }

    @PostMapping("/theatre-owner/theatre/{theatreId}/assets") 
    public ResponseEntity<?> uploadAssets(
        @RequestPart("logo") MultipartFile logo,
        @RequestPart("images") List<MultipartFile> images,
        @PathVariable String theatreId
     ){

        Map<String,Object> data =new HashMap<>();
        data.put("data",  theatreService.uploadAssets(logo, images, theatreId));
        return Response.buildSuccessResponse("Images Uploaded Successfully",HttpStatus.CREATED ,data);

     }

     @GetMapping("/theatres") 
     public ResponseEntity<?> getTheatres(){

        Map<String,Object> data = new HashMap<>();
        data.put("data", theatreService.getAllTheatres());
        return Response.buildSuccessResponse("Theatres Retrieved Successfully", HttpStatus.OK, data);

     }


    

}
