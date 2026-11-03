package com.Sk.BookMyShow.Entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.Sk.BookMyShow.DTOs.ImageInfo;
import com.Sk.BookMyShow.Enums.TheatreStatus;

import java.time.Instant;
import java.util.List;

@Getter
@Setter  
@Document(collection = "theatres")
@Builder 
@CompoundIndex(
    name = "theatre_location_idx",
    def = "{'name': 1, 'city': 1, 'state': 1, 'pincode': 1}"
)
public class Theatre {

    @Id
    private ObjectId id;

    private String name;
    private String description;

    @Indexed 
    private String ownerId;

    private String addressLine;
    private String landmark;
    private String city;
    private String state;
    private String country;
    private String pincode;

    @GeoSpatialIndexed (type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location;

    private List<String> facilities;
    private String contactNumber;
    
    private String email;
    private ImageInfo logo;
    private List<ImageInfo> imageUrls;

    private TheatreStatus status;

    @CreatedDate 
    private Instant createdAt;

    @LastModifiedDate 
    private Instant updatedAt;
}
