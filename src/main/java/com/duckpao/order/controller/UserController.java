package com.duckpao.order.controller;

import com.duckpao.order.adapter.UserAdapter;
import com.duckpao.order.dto.request.CreateUserRequest;
import com.duckpao.order.dto.response.UserResponse;
import com.duckpao.order.model.User;
import com.duckpao.order.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ✅ POST /api/users
    @PostMapping
    public UserResponse createUser(@RequestBody CreateUserRequest request) {
      return userService.create(request);
    }

    // ✅ GET /api/users
    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(UserAdapter::toResponse)
                .collect(Collectors.toList());
    }

    // ✅ GET /api/users/{id}
    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable Long id) {
        User user = userService.getById(id);
        return UserAdapter.toResponse(user);
    }
}
