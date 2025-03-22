package com.vishnu.secureshare.mapper;

import com.vishnu.secureshare.dto.UserResponse;
import com.vishnu.secureshare.entity.User;

public class UserMapper {
    public static UserResponse toDto(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
    }
}