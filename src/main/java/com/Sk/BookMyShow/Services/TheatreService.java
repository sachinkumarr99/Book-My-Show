package com.Sk.BookMyShow.Services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.Sk.BookMyShow.DTOs.ImageInfo;
import com.Sk.BookMyShow.DTOs.TheatreDto;
import com.Sk.BookMyShow.Entity.Theatre;
import com.Sk.BookMyShow.Enums.TheatreStatus;
import com.Sk.BookMyShow.Exceptions.DuplicateResourceException;
import com.Sk.BookMyShow.Exceptions.ResourceNotFoundException;
import com.Sk.BookMyShow.Mapper.TheatreMapper;
import com.Sk.BookMyShow.Repositories.TheatreRepo;
import com.Sk.BookMyShow.Utils.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class TheatreService {
    
    private final TheatreRepo theatreRepo;
    private final ImageService imageService;
    private final TheatreMapper theatreMapper;

    

    public Theatre registerTheatre(TheatreDto theatreDto){
        if(theatreRepo.existsByNameAndCityAndStateAndPincode(theatreDto.getName(), theatreDto.getCity(), theatreDto.getState(),theatreDto.getPincode())){
            throw new DuplicateResourceException(theatreDto.getName()+" already exists in this city");
        }
        Theatre theatre = theatreMapper.toTheatre(theatreDto);
        theatre.setOwnerId(SecurityUtils.getCurrentLoggedUserId());
        theatre.setStatus(TheatreStatus.ACTIVE);
        return theatreRepo.save(theatre);
        
    }

    private ImageInfo uploadLogo(MultipartFile logo,Theatre theatre){
    
        ImageInfo imageInfo = imageService.upload(logo, "BookMyShow/users/user_"+SecurityUtils.getCurrentLoggedUserId()+"/theatres/theatre_"+String.valueOf(theatre.getId())+"/logo");
        theatre.setLogo(imageInfo);
        theatreRepo.save(theatre);
        return imageInfo;
    }

    private List<ImageInfo> uploadTheatreImages(List<MultipartFile> images,Theatre theatre){

        
        List<ImageInfo> imageInfo = imageService.uploadFiles(images, "BookMyShow/users/user_"+SecurityUtils.getCurrentLoggedUserId()
                                    +"/theatres/theatre_"+String.valueOf(theatre.getId())+"/images");

        theatre.setImageUrls(imageInfo);
        theatreRepo.save(theatre);
        return imageInfo;
    }

    public Map<String,Object> uploadAssets(MultipartFile logo,List<MultipartFile> images,String theatreId){

        // 
         Theatre theatre = theatreRepo.findById(new ObjectId(theatreId)).orElseThrow(()-> new ResourceNotFoundException("Invalid Theatre Id "));        
         if(!theatre.getOwnerId().equals(SecurityUtils.getCurrentLoggedUserId())){
            throw new ResourceNotFoundException("Theatre Not Found");
         }
         ImageInfo uploadedLogo = uploadLogo(logo, theatre);
         List<ImageInfo> uploadedTheatreImages = uploadTheatreImages(images, theatre);

         Map<String,Object> uploadedImages = new HashMap<>();
         uploadedImages.put("logo", uploadedLogo);
         uploadedImages.put("images", uploadedTheatreImages);

         return uploadedImages;

    }
    
    public List<Theatre> getAllTheatres(){
        return theatreRepo.findAll();
    }


    
}
