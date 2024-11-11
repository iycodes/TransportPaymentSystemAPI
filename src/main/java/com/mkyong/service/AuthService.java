package com.mkyong.service;

import com.mkyong.configs.WebConfig;
import com.mkyong.model.SessionEntity;
import com.mkyong.model.UserEntity;
import com.mkyong.model.UserEntity.UserRole;
import com.mkyong.model.VerificationCodeEntity;
import com.mkyong.model.dtos.AuthDataDto;
import com.mkyong.model.dtos.LoginUserDto;
import com.mkyong.model.dtos.LogoutDto;
import com.mkyong.model.dtos.RegisterUserDto;
import com.mkyong.model.dtos.VerifyEmailDto;
import com.mkyong.repository.SessionRepository;
import com.mkyong.repository.UserRepository;
import com.mkyong.repository.VerificationCodeRepository;
import com.mkyong.responses.LoginResponse;
import com.mkyong.responses.VerifyEmailResponse;

import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final SessionRepository sessionRepository;
    private final JWTService jwtService;
    @Autowired
    private VerificationCodeRepository verificationCodeRepository;
    @Autowired
    private WebConfig webConfig;

    public AuthService(
            UserRepository userRepository_, PasswordEncoder passwordEncoder_,
            AuthenticationManager authenticationManager_, SessionRepository sessionRepository_,
            JWTService jwtService_) {
        this.authenticationManager = authenticationManager_;
        this.passwordEncoder = passwordEncoder_;
        this.userRepository = userRepository_;
        this.sessionRepository = sessionRepository_;
        this.jwtService = jwtService_;
    }

    public ArrayList<SessionEntity> getSessions(String userId) {
        return sessionRepository.findByUserId(userId);
    }

    public Optional<SessionEntity> getSessionByToken(String refreshToken) {
        return sessionRepository.findByRefreshToken(refreshToken);
    }

    public LoginResponse login(LoginUserDto dto, String userId) {

        System.out.println("login function in service  is running");
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                String.valueOf(userId),
                dto.getPassword()));

        UserEntity authenticatedUser = userRepository.findByEmail(dto.getEmail()).orElseThrow();
        // authenticatedUser.setPassword(null);
        // authenticatedUser.setPassword(null);

        System.out.println("authenticated user id is =>> " + authenticatedUser.getUsername());
        String jwtAccessToken = jwtService.generateToken(authenticatedUser);
        String refreshToken = jwtService.generateRefreshToken(new HashMap<>(),
                authenticatedUser);
        System.out.println("new session, fcm token is " + dto.getFcmToken());
        SessionEntity sessionEntity = new SessionEntity(userId, refreshToken, "", dto.getFcmToken());
        sessionRepository.save(sessionEntity);
        AuthDataDto authDataDto = new AuthDataDto();
        authDataDto.setJwtAccessToken(jwtAccessToken);
        authDataDto.setRefreshToken(refreshToken);
        authDataDto.setUserId(userId);
        authDataDto.setSessionId(sessionEntity.getId());
        // authDataDto.setUserEntity(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse(jwtService.getRefreshTokenExpirationTime(),
                authDataDto, authenticatedUser);
        // loginResponse.authDataDto.setjwtAccessToken(jwtAccessToken);
        // loginResponse.authDataDto.setRefreshToken(refreshToken);
        // loginResponse.setUserInfo(authenticatedUser);
        // loginResponse.setExpiresIn(jwtService.getRefreshTokenExpirationTime());
        // loginResponse.authDataDto.setSessionId(sessionEntity.getId());
        System.out.println("login response being sent is " + loginResponse.toString());
        return loginResponse;
    }

    public UserEntity register(RegisterUserDto dto) {
        UserEntity user = new UserEntity();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPhoneNo(dto.getPhoneNo());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return userRepository.save(user);
    }

    public UserEntity registerDriver(RegisterUserDto dto) {
        UserEntity user = new UserEntity();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPhoneNo(dto.getPhoneNo());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(UserRole.driver);
        return userRepository.save(user);
    }

    public boolean validateVerificationCode(String email, int codeInputByUser) {
        Optional<VerificationCodeEntity> verificationCodeEntity = verificationCodeRepository
                .findByEmail(email);
        if (verificationCodeEntity.isEmpty())
            return false;
        else if (verificationCodeEntity.get().getCode() == codeInputByUser) {
            verificationCodeRepository.deleteById(verificationCodeEntity.get().getId());
            return true;
        } else {
            return false;
        }
    }

    public void handleVerificationCode(VerifyEmailDto dto) {
        Random random = new Random();

        Optional<VerificationCodeEntity> verificationCodeEntity = verificationCodeRepository
                .findByEmail(dto.getRecepientEmail());
        if (verificationCodeEntity.isEmpty()) {
            System.out.println("No verification code found in server");
            int randomNumber = random.nextInt(1000, 9999);
            verificationCodeRepository.save(new VerificationCodeEntity(randomNumber, dto.getRecepientEmail()));
            dto.setCode(randomNumber);
        } else if (verificationCodeEntity.get().getExpiresIn().isBefore(LocalDateTime.now())) {
            System.out.println("verification code found in server but expired!");
            int randomNumber = random.nextInt(1000, 9999);
            verificationCodeEntity.get().setCode(randomNumber);
            verificationCodeEntity.get().setExpiresIn(LocalDateTime.now().plusMinutes(30));
            // verificationCodeRepository.verificationCodeRepository.deleteById(verificationCodeEntity.get().getId());
            verificationCodeRepository.save(verificationCodeEntity.get());
            dto.setCode(randomNumber);
        } else {
            System.out.println("An existing verification code was found in the server");
            dto.setCode(verificationCodeEntity.get().getCode());
        }
    }

    public boolean sendVerificationEmail(VerifyEmailDto dto) {
        handleVerificationCode(dto);
        try {
            Mono<ResponseEntity<VerifyEmailResponse>> request = webConfig.webClient().post()
                    .uri("/send_mail").accept(MediaType.APPLICATION_JSON).body(Mono.just(dto),
                            VerifyEmailDto.class)
                    .retrieve().toEntity(VerifyEmailResponse.class);
            ResponseEntity<VerifyEmailResponse> response = request.block();
            if (response.getStatusCode().value() < 300) {
                // System.out.println("mail sent succesfully");
                return true;
            } else {
                // System.out.println("error sending the mail oo");
                return false;
            }
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    public String logout(LogoutDto dto) {
        Optional<SessionEntity> sessionEntity = sessionRepository.findById(dto.getSessionId());
        if (sessionEntity.isEmpty()) {
            return "not found";
        }
        if (sessionEntity.get().getUserId() != dto.getUserId()) {
            return "unauthrized logout";
        }
        try {
            sessionRepository.delete(sessionEntity.get());
        } catch (Exception e) {
            // TODO: handle exception
            return "internal server error";
        }
        return "success, logged out succesfully";
    }

}
