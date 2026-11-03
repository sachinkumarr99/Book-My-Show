package com.Sk.BookMyShow.Filters;

import java.io.IOException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.Sk.BookMyShow.Entity.CustomUserDetails;
import com.Sk.BookMyShow.Exceptions.ExceptionHandler.JwtAuthenticationEntryPoint;
import com.Sk.BookMyShow.Services.CustomUserDetailsService;
import com.Sk.BookMyShow.Services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationEntryPoint entryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // step 1 : get authorization header from request
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);

        String userName;
        try {
            userName = jwtService.extractClaim(token, Claims::getSubject);
        } catch (JwtException | IllegalArgumentException e) {
            SecurityContextHolder.clearContext();

            entryPoint.commence(
                    request,
                    response,
                    new BadCredentialsException("Invalid JWT", e));
            return;
        }

        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(userName);
        UsernamePasswordAuthenticationToken authObj = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authObj.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authObj);

        filterChain.doFilter(request, response);


    }

}
