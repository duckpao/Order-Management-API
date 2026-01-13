package com.duckpao.order.service;


import com.duckpao.order.adapter.ProductAdapter;
import com.duckpao.order.adapter.UserAdapter;
import com.duckpao.order.dto.request.CreateProductRequest;
import com.duckpao.order.dto.request.CreateUserRequest;
import com.duckpao.order.dto.response.ProductResponse;
import com.duckpao.order.dto.response.UserResponse;
import com.duckpao.order.model.Product;
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

    public UserResponse create(CreateUserRequest request) {
      User user = UserAdapter.toModel(request);
      User savedUser = userRepository.save(user);
        return UserAdapter.toResponse(savedUser);
    }
//    public ProductResponse create(CreateProductRequest request) {
//        Product product = ProductAdapter.toModel(request);
//        Product saved = productRepository.save(product);
//        return ProductAdapter.toResponse(saved);
//    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not found"));
    }
}
