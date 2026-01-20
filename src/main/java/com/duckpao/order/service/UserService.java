package com.duckpao.order.service;

import com.duckpao.order.adapter.UserAdapter;
import com.duckpao.order.domain.UserDomainService;
import com.duckpao.order.dto.request.CreateUserRequest;
import com.duckpao.order.dto.response.UserResponse;
import com.duckpao.order.repository.UserRepository;
import com.duckpao.order.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

import com.duckpao.order.exception.BusinessException;

@RequiredArgsConstructor
@Log4j2
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserAdapter userAdapter;
    private final UserDomainService userDomainService;
    public UserResponse create(CreateUserRequest request) {
        log.debug(request.toString());
        User user = userAdapter.toModel(request);
        userDomainService.validateEmail(user.getEmail());
        log.info("Creating user with email " + user.getEmail());
        User savedUser = userRepository.save(user);
        return userAdapter.toResponse(savedUser);
    }

    public List<User> getAllUsers() {
        log.info("Retrieving all users");
        return userRepository.findAll();
    }

    public User getById(Long id) {
        log.info("Retrieving user with id " + id);
        return userRepository.findById(id).orElseThrow(() -> BusinessException.badRequest("User not found","User not found with id=" + id));
    }
}
