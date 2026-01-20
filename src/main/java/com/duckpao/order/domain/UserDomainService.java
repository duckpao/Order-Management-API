package com.duckpao.order.domain;
import com.duckpao.order.exception.BusinessException;
import com.duckpao.order.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
@Log4j2
public class UserDomainService {
    private final UserRepository userRepository;


    public  void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            log.error("User with email={} already exists", email);
            throw BusinessException.badRequest("EMAIL_EXISTED", "Email already exists");

        }

    }
}
