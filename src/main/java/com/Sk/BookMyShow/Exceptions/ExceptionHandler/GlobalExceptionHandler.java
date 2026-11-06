package com.Sk.BookMyShow.Exceptions.ExceptionHandler;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.Sk.BookMyShow.Entity.Response;
import com.Sk.BookMyShow.Exceptions.DuplicateResourceException;
import com.Sk.BookMyShow.Exceptions.ResourceNotFoundException;
import com.Sk.BookMyShow.Exceptions.ShowException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice 
@Slf4j 
public class GlobalExceptionHandler {

    private final DateTimeFormatter datetTimeFormatter = DateTimeFormatter
                                .ofPattern("dd-MM-yyyy HH:mm:ss")
                                .withZone(ZoneId.of("Asia/Kolkata"));
    

    public ResponseEntity<Response<String,Object>> build(String msg,HttpStatus httpStatus,HttpServletRequest request){
        log.error("Error : {}",msg);
        return  ResponseEntity.status(httpStatus).body(
                Response.<String, Object>builder()
                .success(false)
                .message(msg)
                .timeStamp(datetTimeFormatter.format(Instant.now()))
                .path(request.getRequestURI())
                .statusCode(httpStatus.value())
                .build());
    }


    @ExceptionHandler({
        ResourceNotFoundException.class,
        UsernameNotFoundException.class
    })
    public ResponseEntity<?> handleResourceNotFoundException(Exception ex,HttpServletRequest request){

       return build(ex.getMessage(),HttpStatus.NOT_FOUND,request);
    }

    @ExceptionHandler (BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex,HttpServletRequest request){
        
       return build(ex.getMessage(),HttpStatus.UNAUTHORIZED, request);
    }


    @ExceptionHandler (DuplicateResourceException.class)
    public ResponseEntity<?> handleBadCredentialsException(DuplicateResourceException ex,HttpServletRequest request){
        
       return build(ex.getMessage(),HttpStatus.CONFLICT, request);
    }


    @ExceptionHandler (MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,HttpServletRequest request){

        String msg = ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(e -> e.getField()+" : "+e.getDefaultMessage())
                        .collect(Collectors.joining(", "));
        
        return build(msg, HttpStatus.BAD_REQUEST, request);
    }


    @ExceptionHandler (AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex,HttpServletRequest request){
        
       return build(ex.getMessage(),HttpStatus.FORBIDDEN, request);
    }
    
    @ExceptionHandler(ShowException.class)
    public ResponseEntity<Response<String, Object>> handleShowException(
            ShowException ex, HttpServletRequest request) {
        log.error("Error occur while doing request at {} due to : {}", request.getRequestURI(), ex.getMessage());
        return build(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<String, Object>> handleAll(
            Exception ex, HttpServletRequest request) {
        log.error("Unexpected error at {}", request.getRequestURI(), ex);
        return build("Something went wrong ",HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


    

}
