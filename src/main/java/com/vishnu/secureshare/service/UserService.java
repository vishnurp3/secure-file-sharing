package com.vishnu.secureshare.service;

import com.vishnu.secureshare.dto.UserLoginRequest;
import com.vishnu.secureshare.dto.UserRegistrationRequest;
import com.vishnu.secureshare.dto.UserResponse;

public interface UserService {

    UserResponse register(UserRegistrationRequest request);

    String login(UserLoginRequest request);
}
