package com.Sk.BookMyShow.Controllers;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Sk.BookMyShow.DTOs.UserDto;
import com.Sk.BookMyShow.DTOs.Requests.LogInRequestDto;
import com.Sk.BookMyShow.Entity.Response;
import com.Sk.BookMyShow.Services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("/public")
@RequiredArgsConstructor 
public class PublicController {


    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDto userDto){

        return ResponseEntity.status(HttpStatus.CREATED).body(
            userService.registerUser(userDto)
        );
    }

    
    @PostMapping("/logIn")
    public ResponseEntity<?> logIn(@Valid @RequestBody LogInRequestDto loginDto){

        HashMap<String,String> data = new HashMap<>();
        data.put("token",userService.logIn(loginDto));

        return ResponseEntity.status(HttpStatus.OK).body(
             Response.<String, String >builder()
                .success(true)
                .data(data)
                .timeStamp(
                        DateTimeFormatter
                                .ofPattern("dd-MM-yyyy HH:mm:ss")
                                .withZone(ZoneId.of("Asia/Kolkata"))
                                .format(Instant.now()))
                .statusCode(HttpStatus.OK.value())
                .build()
        
        );
    }

    
}
