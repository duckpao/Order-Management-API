package com.duckpao.order.service;

import com.duckpao.order.adapter.UserAdapter;
import com.duckpao.order.dto.request.CreateUserRequest;
import com.duckpao.order.repository.UserRepository;
import com.duckpao.order.model.User;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User create(CreateUserRequest request){
         User user = UserAdapter.toModel(request);
        return userRepository.save(user);

    }
    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }
    public User getAllUsers(){
        return userRepository.findAll().stream().findFirst().get();
    }

}
