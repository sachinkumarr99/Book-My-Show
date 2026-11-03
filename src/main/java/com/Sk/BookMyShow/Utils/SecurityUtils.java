package com.Sk.BookMyShow.Utils;


import org.springframework.security.core.context.SecurityContextHolder;
import com.Sk.BookMyShow.Entity.CustomUserDetails;


public class SecurityUtils {

    private static final CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    public static String getCurrentLoggedUserId(){
        return userDetails.getId();
    }


}
