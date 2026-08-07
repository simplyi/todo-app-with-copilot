package com.appsdeveloperblog.todo.demo.service;

import com.appsdeveloperblog.todo.demo.dto.UserRegistrationDto;
import com.appsdeveloperblog.todo.demo.entity.User;
import com.appsdeveloperblog.todo.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(final UserRegistrationDto dto) {
        final User user = new User();
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        return userRepository.save(user);
    }
}
