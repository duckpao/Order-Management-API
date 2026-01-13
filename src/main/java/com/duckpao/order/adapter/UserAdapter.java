package com.duckpao.order.adapter;

import com.duckpao.order.common.UserStatus;
import com.duckpao.order.dto.request.CreateUserRequest;
import com.duckpao.order.dto.response.UserResponse;
import com.duckpao.order.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserAdapter {
    // DTO -> Model
    public User  toModel(CreateUserRequest dto) {
        return  User.builder().name(dto.getName())
                .email(dto.getEmail())
                .status(UserStatus.ACTIVE)
                .build();
    }

    // Model -> DTO
    public UserResponse toResponse(User user) {
        return  UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .status(user.getStatus().name())
                .build();
    }
}
