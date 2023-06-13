package com.example.ourapp;

import com.example.ourapp.dto.UserDto;
import com.example.ourapp.model.User;
import com.example.ourapp.repository.RoleRepository;
import com.example.ourapp.repository.UserRepository;
import com.example.ourapp.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
class OurappApplicationTests {
	@Test
	void contextLoads() {
	}
}
