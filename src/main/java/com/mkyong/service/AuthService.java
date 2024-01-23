package com.mkyong.service;

import com.mkyong.model.Userr;
import com.mkyong.model.dtos.LoginUserDto;
import com.mkyong.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public AuthService(
            UserRepository userRepository_, PasswordEncoder passwordEncoder_, AuthenticationManager authenticationManager_
    ) {
        this.authenticationManager = authenticationManager_;
        this.passwordEncoder = passwordEncoder_;
        this.userRepository = userRepository_;
    }

    public Userr login(LoginUserDto dto) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                dto.getEmail(),
                dto.getPassword()
        ));

        return userRepository.findByEmail(dto.getEmail()).orElseThrow();
    }
}
