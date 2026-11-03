package com.Sk.BookMyShow.Services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Sk.BookMyShow.DTOs.UserDto;
import com.Sk.BookMyShow.DTOs.Requests.LogInRequestDto;
import com.Sk.BookMyShow.Entity.CustomUserDetails;
import com.Sk.BookMyShow.Entity.User;
import com.Sk.BookMyShow.Enums.Role;
import com.Sk.BookMyShow.Exceptions.DuplicateResourceException;
import com.Sk.BookMyShow.Mapper.UserMapper;
import com.Sk.BookMyShow.Repositories.UserRepo;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class UserService{
    
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final CustomUserDetailsService customUserDetailsService;


    public User registerUser(UserDto userDto){

        if(userRepo.existsByEmail(userDto.getEmail())){
            throw new DuplicateResourceException("Email Already Exists");
        }

        User newUser = userMapper.toUser(userDto);
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        newUser.setRole(Role.USER);

        return userRepo.save(newUser);

    }

    public String logIn(LogInRequestDto logInRequestDto){
        
        if(!userRepo.existsByEmail(logInRequestDto.getEmail())) throw new UsernameNotFoundException("Email Not Exists");

        Authentication authObj = new UsernamePasswordAuthenticationToken(logInRequestDto.getEmail(), logInRequestDto.getPassword());
        authManager.authenticate(authObj);

        CustomUserDetails userDetails = (CustomUserDetails)customUserDetailsService.loadUserByUsername(logInRequestDto.getEmail());

        Map<String,Object> claims = new HashMap<>();
        claims.put("userName", userDetails.getName());
        claims.put("role", userDetails.getUserRole());

        return jwtService.generateAccessToken(logInRequestDto.getEmail(), claims);

    }

  


}
