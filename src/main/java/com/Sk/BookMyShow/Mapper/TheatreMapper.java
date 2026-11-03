package com.Sk.BookMyShow.Mapper;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Component;

import com.Sk.BookMyShow.DTOs.TheatreDto;
import com.Sk.BookMyShow.Entity.Theatre;

@Component 
public class TheatreMapper {
    
    public Theatre toTheatre(TheatreDto theatreDto){

         return Theatre.builder()
                .name(theatreDto.getName())
                .description(theatreDto.getDescription())
                .addressLine(theatreDto.getAddressLine())
                .landmark(theatreDto.getLandmark())
                .city(theatreDto.getCity())
                .state(theatreDto.getState())
                .country(theatreDto.getCountry())
                .pincode(theatreDto.getPincode())
                .location(
                    new GeoJsonPoint(
                        theatreDto.getLongitude(),
                        theatreDto.getLatitude()
                    )
                )
                .facilities(theatreDto.getFacilities())
                .contactNumber(theatreDto.getContactNumber())
                .email(theatreDto.getEmail())
                .build();
    
    }

    
}
