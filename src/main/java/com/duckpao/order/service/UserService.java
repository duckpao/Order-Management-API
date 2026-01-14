package com.duckpao.order.service;

import com.duckpao.order.adapter.UserAdapter;
import com.duckpao.order.dto.request.CreateUserRequest;
import com.duckpao.order.dto.response.UserResponse;
import com.duckpao.order.repository.UserRepository;
import com.duckpao.order.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import com.duckpao.order.exception.BusinessException;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserAdapter userAdapter;

    public UserResponse create(CreateUserRequest request) {
        User user = userAdapter.toModel(request);
        User savedUser = userRepository.save(user);
        return userAdapter.toResponse(savedUser);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new BusinessException("User not found"));
    }
}
