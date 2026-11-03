package com.Sk.BookMyShow.Mapper;

import org.springframework.stereotype.Component;

import com.Sk.BookMyShow.DTOs.CreateScreenRequestDto;
import com.Sk.BookMyShow.Entity.Screen;

@Component 
public class ScreenMapper {
    
     public Screen toEntity(CreateScreenRequestDto request, String theatreId) {

        return Screen.builder()
                .theatreId(theatreId)
                .name(request.getName())
                .totalRows(request.getTotalRows())
                .totalColumns(request.getTotalColumns())
                .isActive(true)
                .build();
    }
}
