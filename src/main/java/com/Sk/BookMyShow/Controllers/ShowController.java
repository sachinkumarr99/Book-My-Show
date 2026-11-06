package com.Sk.BookMyShow.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.Sk.BookMyShow.DTOs.CreateShowRequestDTO;
import com.Sk.BookMyShow.Entity.Response;
import com.Sk.BookMyShow.Services.ShowService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor 
public class ShowController {
    
    private final ShowService showService;

    @PostMapping ("theatre-owner/theatre/{theatreId}/screen/{screenId}/show")
    public ResponseEntity<?> createShow(@Valid @RequestBody CreateShowRequestDTO showRequestDTO,
                                        @PathVariable String theatreId,
                                        @PathVariable String screenId
    ){

        Map<String,Object> data = new HashMap<>();
        data.put("data", showService.createShow(showRequestDTO, theatreId, screenId));
        return Response.buildSuccessResponse("Show Created Successfully", HttpStatus.OK, data);

    }
}
