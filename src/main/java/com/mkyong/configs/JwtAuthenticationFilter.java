package com.mkyong.configs;

import com.mkyong.service.AuthenticationService;
import com.mkyong.service.JWTService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.awt.PageAttributes.MediaType;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JWTService jwtService,
            UserDetailsService userDetailsService,
            HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        // System.out.println("auth header is " + authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("NO AUTH TOKEN FOUND");
            filterChain.doFilter(request, response);
            return;
        }
        try {
            final String jwt = authHeader.substring(7);

            System.out.println("jwt in authHeader is " + jwt);
            final String userId = jwtService.extractUsername(jwt);
            // System.out.println("user id is " + userId);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userId != null && authentication == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userId);
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // System.out.println("jwt token is valid");
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
                // try {
                // UserDetails userDetails = this.userDetailsService.loadUserByUsername(userId);
                // if (jwtService.isTokenValid(jwt, userDetails)) {
                // // System.out.println("jwt token is valid");
                // UsernamePasswordAuthenticationToken authToken = new
                // UsernamePasswordAuthenticationToken(
                // userDetails,
                // null,
                // userDetails.getAuthorities());

                // authToken.setDetails(new
                // WebAuthenticationDetailsSource().buildDetails(request));
                // SecurityContextHolder.getContext().setAuthentication(authToken);
                // // System.out.println("auth token is " + authToken.toString());
                // }
                // } catch (Exception e) {
                // // TODO: handle exception

                // System.err.println("unable to load user , user not found");
                // }
                ;
                // System.out.println("user detail is " + userDetails.toString());

            }

            filterChain.doFilter(request, response);
        } catch (Exception exception) {

            // if (exception instanceof ExpiredJwtException) {
            // // response.getWriter().write(exception.getMessage());
            // //
            // response.setContentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE);
            // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            // handlerExceptionResolver.resolveException(request, response, null,
            // exception);
            // // return;
            // // System.out.println("respon is " + response.set);
            // }

            // already handled various exceptions types in my GlobalExceptionHandler file
            response.setStatus(403);
            System.out.println("error validating jwt");
            // throw ;
            handlerExceptionResolver.resolveException(request, response, null,
                    new JwtException("Error during jwt validation or authentication"));
        }
    }
}