package com.Sk.BookMyShow.Mapper;

import java.time.Instant;

import org.springframework.stereotype.Component;

import com.Sk.BookMyShow.DTOs.CreateShowRequestDTO;
import com.Sk.BookMyShow.Entity.Show;

@Component
public class ShowMapper {

    public Show toEntity(CreateShowRequestDTO request) {

        return Show.builder()
                .theatreId(request.getTheatreId())
                .screenId(request.getScreenId())
                .showName(request.getShowName())
                .showDate(request.getShowDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .language(request.getLanguage())
                .basePrice(request.getBasePrice())
                .createdAt(Instant.now())
                .build();
    }
}