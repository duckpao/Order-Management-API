package com.duckpao.order.controller;

import com.duckpao.order.entity.User;
import com.duckpao.order.exception.BusinessException;
import com.duckpao.order.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import com.duckpao.order.dto.request.CreateUserRequest;
import java.util.List;
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ POST /api/users
    @PostMapping
    public User createUser(@RequestBody CreateUserRequest request) {
        User user = new User(request.getName(), request.getEmail());
        return userRepository.save(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    // ✅ GET /api/users/{id}
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not found"));
    }


}
