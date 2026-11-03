package com.Sk.BookMyShow.Entity;

import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.Sk.BookMyShow.Enums.Role;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
public class CustomUserDetails implements UserDetails{

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public @Nullable String getPassword() {
       return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    public Role getUserRole(){
        return user.getRole();
    }

    public String getName(){
        return user.getName();
    }

    public String getId(){
        return String.valueOf(user.getId());
    }
    
}
