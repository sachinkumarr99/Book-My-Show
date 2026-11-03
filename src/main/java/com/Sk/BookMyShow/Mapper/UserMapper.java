package com.Sk.BookMyShow.Mapper;

import org.springframework.stereotype.Component;

import com.Sk.BookMyShow.DTOs.UserDto;
import com.Sk.BookMyShow.Entity.User;

@Component 
public class UserMapper {
    
    public User toUser(UserDto userDto){
         
        return User.builder()
                    .name(userDto.getName())
                    .email(userDto.getEmail())
                    .password(userDto.getPassword())
                    .build();
    }

    public UserDto toUserDto(User user){
         
        return UserDto.builder()
                    .name(user.getName())
                    .email(user.getEmail())
                    .build();
    }
}
