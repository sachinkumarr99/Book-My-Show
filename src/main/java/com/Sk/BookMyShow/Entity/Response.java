package com.Sk.BookMyShow.Entity;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Data;
 


@Data
@Builder 
public class Response<K,V> {
    
    private Boolean success;
    private String message;
    private int statusCode;
    private Map<K,V> data;
    private String timeStamp;
    private String path;



    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                                .ofPattern("dd-MM-yyyy HH:mm:ss")
                                .withZone(ZoneId.of("Asia/Kolkata"));
    

    public static ResponseEntity<Response<String,Object>> buildSuccessResponse(String msg,HttpStatus httpStatus,Map<String,Object> data){
        
        return  ResponseEntity.status(httpStatus).body(
                Response.<String, Object>builder()
                .success(true)
                .message(msg)
                .timeStamp(dateTimeFormatter.format(Instant.now()))
                .data(data)
                .statusCode(httpStatus.value())
                .build());
    }

    
}
