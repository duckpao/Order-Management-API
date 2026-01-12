package com.duckpao.order.adapter;

import com.duckpao.order.common.UserStatus;
import com.duckpao.order.dto.request.CreateUserRequest;
import com.duckpao.order.dto.response.UserResponse;
import com.duckpao.order.model.User;

public class UserAdapter {

    private UserAdapter() {}

    // DTO -> Model
    public static User  toModel(CreateUserRequest dto) {
        return  User.builder().name(dto.getName())
                .email(dto.getEmail())
                .status(UserStatus.ACTIVE)
                .build();
    }

    // Model -> DTO
    public static UserResponse toResponse(User user) {
        return  UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .status(user.getStatus().name())
                .build();
    }
}
