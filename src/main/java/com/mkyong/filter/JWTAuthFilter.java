package com.mkyong.filter;

import com.mkyong.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    public JWTAuthFilter(
            HandlerExceptionResolver handlerExceptionResolver_,
    JWTService jwtService_,
    UserDetailsService userDetailsService_
    ){

        this.jwtService = jwtService_;
        this.handlerExceptionResolver = handlerExceptionResolver_;
        this.userDetailsService = userDetailsService_;
    }

    protected void doFilterInternal(


            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !(authHeader.startsWith("Bearer"))) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            final String jwtToken = authHeader.substring(7);
            final String userEmail = jwtService.extractUsername(jwtToken);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(userEmail !=null && authentication == null){
                User user = this.userDetailsService.loadUserByUsername(userEmail);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
