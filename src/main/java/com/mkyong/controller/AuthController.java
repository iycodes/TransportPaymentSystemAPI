package com.mkyong.controller;


import com.mkyong.service.JWTService;
import com.mkyong.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String welcome(){
        return "This is a welcome message. \n this endpoint is not secure";
    }

//    @PostMapping("/generateToken")
//    public String authenticateAndGetToken(@RequestBody) {
//
//    }


}


