package com.vishnu.secureshare.service.impl;

import com.vishnu.secureshare.dto.UserLoginRequest;
import com.vishnu.secureshare.dto.UserRegistrationRequest;
import com.vishnu.secureshare.dto.UserResponse;
import com.vishnu.secureshare.entity.User;
import com.vishnu.secureshare.mapper.UserMapper;
import com.vishnu.secureshare.repository.UserRepository;
import com.vishnu.secureshare.security.JwtUtil;
import com.vishnu.secureshare.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserResponse register(UserRegistrationRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role("USER")
                .build();
        User saved = userRepository.save(user);
        return UserMapper.toDto(saved);
    }

    @Override
    public String login(UserLoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtUtil.generateToken(userDetails);
    }
}