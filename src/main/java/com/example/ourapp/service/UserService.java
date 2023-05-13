package com.example.ourapp.service;

import com.example.ourapp.dto.UserDto;
import com.example.ourapp.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
}
