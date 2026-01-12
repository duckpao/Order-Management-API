package com.duckpao.order.service;


import com.duckpao.order.dto.request.CreateUserRequest;
import com.duckpao.order.repository.UserRepository;
import com.duckpao.order.model.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import com.duckpao.order.exception.BusinessException;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(CreateUserRequest request) {
        User user =  User.builder()
                .name(request.getName())
                .email(request.getEmail()).build();
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not found"));
    }
}
