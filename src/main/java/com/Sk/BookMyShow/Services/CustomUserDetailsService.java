package com.Sk.BookMyShow.Services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.Sk.BookMyShow.Entity.CustomUserDetails;
import com.Sk.BookMyShow.Entity.User;
import com.Sk.BookMyShow.Repositories.UserRepo;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class CustomUserDetailsService implements UserDetailsService{
    
    private final UserRepo userRepo;
     @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       
        User user = userRepo.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User Not Exist"));

        return new CustomUserDetails(user);

    }
}
