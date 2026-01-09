package com.duckpao.order.controller;
import com.duckpao.order.repository.UserRepository;
import com.duckpao.order.entity.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 📌 GET /api/users/{id}
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // 📌 POST /api/users
    @PostMapping
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }
}
