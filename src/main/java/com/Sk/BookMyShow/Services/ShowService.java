package com.Sk.BookMyShow.Services;

import org.springframework.stereotype.Service;

import com.Sk.BookMyShow.DTOs.CreateShowRequestDTO;
import com.Sk.BookMyShow.Entity.Show;
import com.Sk.BookMyShow.Enums.ShowStatus;
import com.Sk.BookMyShow.Exceptions.DuplicateResourceException;
import com.Sk.BookMyShow.Exceptions.ShowException;
import com.Sk.BookMyShow.Mapper.ShowMapper;
import com.Sk.BookMyShow.Repositories.ShowRepo;
import com.Sk.BookMyShow.Utils.CurrencyUtils;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class ShowService {
 
    private final ShowRepo showRepo;
    private final ShowMapper showMapper;


    public CreateShowRequestDTO createShow(CreateShowRequestDTO showRequestDTO,String theatreId,String screenId){

        // check any show overLap or not 
        Show overlapShow = showRepo.overlapShow(screenId,showRequestDTO.getShowDate(), ShowStatus.SCHEDULED,showRequestDTO.getEndTime(), showRequestDTO.getStartTime());
        if(overlapShow != null){
            throw new ShowException("Show scheduled overlap with another SHOW : "+overlapShow.getShowName());
        }

        Show newShow = showMapper.toEntity(showRequestDTO);
        newShow.setTheatreId(theatreId);
        newShow.setScreenId(screenId);
        newShow.setCurrencyCode(CurrencyUtils.getCurrencyCode(showRequestDTO.getCurrencyCode()));

        return showMapper.toDTO(showRepo.save(newShow));
        
    }
}
