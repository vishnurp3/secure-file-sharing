package com.vishnu.secureshare.controller;

import com.vishnu.secureshare.dto.ApiResponse;
import com.vishnu.secureshare.dto.UserLoginRequest;
import com.vishnu.secureshare.dto.UserRegistrationRequest;
import com.vishnu.secureshare.dto.UserResponse;
import com.vishnu.secureshare.service.UserService;
import com.vishnu.secureshare.util.TrackExecutionTime;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @TrackExecutionTime
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody UserRegistrationRequest request) {
        var user = userService.register(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "User registered successfully", user));
    }

    @PostMapping("/login")
    @TrackExecutionTime
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody UserLoginRequest request) {
        var token = userService.login(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", token));
    }
}
