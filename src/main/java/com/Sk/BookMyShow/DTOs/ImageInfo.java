package com.Sk.BookMyShow.DTOs;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder 
@Getter 
@Setter 
public class ImageInfo {
    private String url;
    private String publicId;
}
