package com.duckpao.order.controller;

import com.duckpao.order.adapter.UserAdapter;
import com.duckpao.order.model.User;
import com.duckpao.order.exception.BusinessException;
import com.duckpao.order.repository.UserRepository;
import com.duckpao.order.service.UserService;
import org.springframework.web.bind.annotation.*;
import com.duckpao.order.dto.request.CreateUserRequest;
import java.util.List;
@RestController
@RequestMapping("/api/users")
public class UserController {
private UserService userService;


    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ POST /api/users
    @PostMapping
    public User createUser(@RequestBody CreateUserRequest request) {
        User user = userService.create(request);
        return UserAdapter.toResponse(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return UserService.getAllUser();
    }
    // ✅ GET /api/users/{id}
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not found"));
    }


}
