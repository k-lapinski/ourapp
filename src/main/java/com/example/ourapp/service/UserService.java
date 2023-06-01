package com.example.ourapp.service;

import com.example.ourapp.dto.UserDto;
import com.example.ourapp.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    Optional<User> findUserById(Long id);

    void deleteUser(Long id);

    List<UserDto> findAllUsers();
}
