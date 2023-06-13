package com.example.ourapp;

import com.example.ourapp.dto.UserDto;
import com.example.ourapp.model.Role;
import com.example.ourapp.model.User;
import com.example.ourapp.repository.RoleRepository;
import com.example.ourapp.repository.UserRepository;
import com.example.ourapp.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.MockitoAnnotations.initMocks;


public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserServiceImpl userService;

    @BeforeEach
    public void setup() {
        initMocks(this);
        userService = new UserServiceImpl(userRepository, roleRepository, passwordEncoder);
    }

    @Test
    public void testSaveUser() {
        // Given
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john.doe@example.com");
        userDto.setPassword("password");
        userDto.setDate(LocalDate.now());

        Role role = new Role();
        role.setName("ROLE_USER");
        Mockito.when(roleRepository.findByName("ROLE_USER")).thenReturn(role);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("John Doe");
        savedUser.setEmail("john.doe@example.com");
        savedUser.setDate(LocalDate.now());
        Mockito.when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        userService.saveUser(userDto);

        // Then
        Mockito.verify(userRepository).save(any(User.class));
    }

    @Test
    public void testEditUser() {
        // Given
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john.doe@example.com");
        userDto.setPassword("password");
        userDto.setDate(LocalDate.now());

        Role role = new Role();
        role.setName("ROLE_ADMIN");
        Mockito.when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(role);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("John Doe");
        savedUser.setEmail("john.doe@example.com");
        savedUser.setDate(LocalDate.now());
        Mockito.when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        userService.editUser(userDto, "ROLE_ADMIN");

        // Then
        Mockito.verify(userRepository).save(any(User.class));
    }

    @Test
    public void testFindUserByEmail() {
        // Given
        String email = "john.doe@example.com";
        User user = new User();
        user.setEmail(email);
        Mockito.when(userRepository.findByEmail(email)).thenReturn(user);

        // When
        User result = userService.findUserByEmail(email);

        // Then
        assertEquals(user, result);
    }

    @Test
    public void testFindUserById() {
        // Given
        Long id = 1L;
        User user = new User();
        user.setId(id);
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
        // When
        Optional<User> result = userService.findUserById(id);

        // Then
        assertEquals(Optional.of(user), result);
    }

    @Test
    public void testFindAllUsers() {
        // Given
        User user1 = new User();
        user1.setId(1L);
        user1.setName("John Doe");
        user1.setEmail("john.doe@example.com");
        user1.setDate(LocalDate.now());

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Jane Smith");
        user2.setEmail("jane.smith@example.com");
        user2.setDate(LocalDate.now());

        Mockito.when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // When
        List<UserDto> result = userService.findAllUsers();

        // Then
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
        assertEquals("john.doe@example.com", result.get(0).getEmail());
        assertEquals("Jane", result.get(1).getFirstName());
        assertEquals("Smith", result.get(1).getLastName());
        assertEquals("jane.smith@example.com", result.get(1).getEmail());
    }

    @Test
    public void testDeleteUser() {
        // Given
        String email = "john.doe@example.com";
        User user = new User();
        user.setEmail(email);
        List<User> users = Collections.singletonList(user);
        Mockito.when(userRepository.findAll()).thenReturn(users);

        // When
        userService.deleteUser(email);

        // Then
        Mockito.verify(userRepository).delete(user);
    }
}