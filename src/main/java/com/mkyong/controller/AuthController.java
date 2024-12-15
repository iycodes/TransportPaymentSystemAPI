package com.mkyong.controller;

import com.mkyong.helpers.MyEmailValidator;
import com.mkyong.model.SessionEntity;
import com.mkyong.model.UserEntity;
import com.mkyong.model.dtos.LoginUserDto;
import com.mkyong.model.dtos.LogoutDto;
import com.mkyong.model.dtos.RegisterUserDto;
import com.mkyong.model.dtos.TokenDto;
import com.mkyong.model.dtos.VerifyEmailDto;
import com.mkyong.responses.ErrorResponse;
import com.mkyong.responses.LoginResponse;
import com.mkyong.responses.VerifyEmailResponse;
import com.mkyong.service.AuthService;
import com.mkyong.service.JWTService;
import com.mkyong.service.UserService;

import io.jsonwebtoken.ExpiredJwtException;

import java.net.http.HttpHeaders;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private final AuthService authenticationService;
    @Autowired
    private final JWTService jwtService;
    @Autowired
    private final UserService userService;

    @Autowired

    public AuthController(
            UserService userService_,
            AuthService authenticationService_,
            JWTService jwtService_,
            AuthenticationManager authenticationManager_) {
        this.authenticationService = authenticationService_;
        this.jwtService = jwtService_;
        this.userService = userService_;

    }

    @GetMapping("/welcome")
    public String welcome() {

        return "This is a welcome message. \n this endpoint is not secure";
    }

    @PostMapping("/registerDriver")
    public ResponseEntity<Object> registerDriver(@RequestBody RegisterUserDto user) {
        boolean isValidated = authenticationService.validateVerificationCode(user.getEmail(),
                user.getVerificationCode());
        if (isValidated) {
            UserEntity userEntity = authenticationService.registerDriver(user);
            LoginUserDto loginUserDto = new LoginUserDto(user.getEmail(), user.getPassword(), user.getFcmToken());
            return ResponseEntity.ok(authenticationService.login(loginUserDto, userEntity.getId()));
        } else {
            System.out.println("Error validating the code");
            return new ResponseEntity<>(new ErrorResponse("Error validating the code"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterUserDto user) {
        boolean isValidated = authenticationService.validateVerificationCode(user.getEmail(),
                user.getVerificationCode());
        if (isValidated) {
            UserEntity userEntity = authenticationService.register(user);
            LoginUserDto loginUserDto = new LoginUserDto(user.getEmail(), user.getPassword(), user.getFcmToken());
            return ResponseEntity.ok(authenticationService.login(loginUserDto, userEntity.getId()));
        } else {
            System.out.println("Error validating the code");
            return new ResponseEntity<>(new ErrorResponse("Error validating the code"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/send_verification_email")
    public ResponseEntity<Object> sendVerificationEmail(@RequestBody VerifyEmailDto dto) {
        if (MyEmailValidator.isEmailValid(dto.getRecepientEmail()) == false) {
            return new ResponseEntity<>(new ErrorResponse("invalid email provided"), HttpStatus.BAD_REQUEST);
        }
        Optional<UserEntity> userOptional = userService.findByEmail(dto.getRecepientEmail());
        if (userOptional.isPresent()) {
            System.out.println("An account with this email exists already");
            return new ResponseEntity<>("email exists already", HttpStatus.CONFLICT);
        }
        boolean isEmailSent = authenticationService.sendVerificationEmail(dto);
        VerifyEmailResponse verifyEmailResponse = new VerifyEmailResponse();
        verifyEmailResponse.setMessage("success response");
        // System.out.println("body received in verify_email api is " + dto.toString());
        if (isEmailSent) {
            return ResponseEntity.ok("");
        } else {
            return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // @PostMapping("/")

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginUserDto dto) {
        System.out.println("Login Request, email is : " + dto.getEmail() + " Password is : " + dto.getPassword());
        Optional<UserEntity> user = userService.findByEmail(dto.getEmail());

        if (user.isEmpty()) {
            return new ResponseEntity<>(new LoginResponse("User does not exist"), HttpStatus.NOT_FOUND);
        }
        System.out.println("user attempting login is " + user.get().toString());
        return ResponseEntity.ok(authenticationService.login(dto, user.get().getId()));
        // return k(new LoginResponse());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutDto dto) {

        System.out.println("attempting logout, data sent is " + dto.toString());

        try {
            String tryLogout = authenticationService.logout(dto);
            switch (tryLogout) {
                case "not found":
                    return new ResponseEntity<>("user not found", HttpStatus.NOT_FOUND);
                // break;
                case "unauthorized":
                    return new ResponseEntity<>("unauthorized", HttpStatus.UNAUTHORIZED);
                // break;
                default:
                    return ResponseEntity.ok("Success, logged out succesfully");
            }

        } catch (Exception e) {
            return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // @PostMapping("/validateJWT")

    // public ResponseEntity<LoginResponse> validateJWT() {
    // jwtService.isTokenValid(null, null);
    // }

    @PostMapping("/refreshJWT")
    public ResponseEntity<String> refreshJWT(@RequestBody TokenDto body) {
        // String prevToken = text;
        System.out.println(
                "refresh token is " + body.getRefreshToken());
        // Optional<user
        String userId = "!";
        String jwtAuthToken = "!";
        System.out.println("refresh token passed to refresh endpoint is " + body.getRefreshToken());
        if (body.getRefreshToken() == "") {
            return new ResponseEntity<>("invalid token", HttpStatus.FORBIDDEN);
        }
        Optional<SessionEntity> sessionEntity = authenticationService.getSessionByToken(body.getRefreshToken());
        if (sessionEntity.isEmpty()) {
            return new ResponseEntity<>("session likely expired or invalidated ", HttpStatus.FORBIDDEN);

        }
        try {
            userId = jwtService.extractUsername(body.getRefreshToken());

        } catch (Exception e) {
            // body.setNewJwtToken(null);
            return new ResponseEntity<>("invalid token", HttpStatus.FORBIDDEN);
        }
        System.out.println("userId requesting new auth token is " + userId);
        UserEntity user = userService.findById(userId).get();
        String newAuthToken = jwtService.generateToken(user);
        System.out.println("newly created auth token is " + newAuthToken);
        org.springframework.http.HttpHeaders responseHeaders = new org.springframework.http.HttpHeaders();
        responseHeaders.add("Authorization", newAuthToken);
        return ResponseEntity.ok(newAuthToken);
        // return ResponseEntity.ok().headers(responseHeaders).body("testing that
        // headers were added!");
    }

    String extractJwtFromHeader(String header) {
        return header.substring(7);

    }

    @GetMapping("/{userId}/sessions")
    ResponseEntity<Object> getUserSessions(@PathVariable String userId) {
        ArrayList<SessionEntity> sessionEntities = authenticationService.getSessions(userId);
        return ResponseEntity.ok(sessionEntities);

    }

    // @PostMapping("/generateToken")
    // public String authenticateAndGetToken(@RequestBody) {
    //
    // }

}
