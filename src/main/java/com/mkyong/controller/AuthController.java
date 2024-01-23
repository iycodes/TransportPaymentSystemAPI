package com.mkyong.controller;


import com.mkyong.model.Userr;
import com.mkyong.model.dtos.LoginUserDto;
import com.mkyong.responses.LoginResponse;
import com.mkyong.service.AuthService;
import com.mkyong.service.JWTService;
import com.mkyong.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    //    @Autowired
    private final AuthService authService;
    @Autowired
    private final JWTService jwtService;
    @Autowired
    private final AuthenticationManager authenticationManager;

    public AuthController(
            AuthService authService_,
            JWTService jwtService_,
            AuthenticationManager authenticationManager_
    ) {
        this.authService = authService_;
        this.authenticationManager = authenticationManager_;
        this.jwtService = jwtService_;
    }

    @GetMapping("/welcome")
    public String welcome() {

        return "This is a welcome message. \n this endpoint is not secure";
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(LoginUserDto dto) {
        Userr authenticatedUser = authService.login(dto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setJwtToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
//        return ResponseEntity.ok(new LoginResponse());

    }

//    @PostMapping("/generateToken")
//    public String authenticateAndGetToken(@RequestBody) {
//
//    }


}


