package com.example.ourapp.repository;

import com.example.ourapp.dto.UserDto;
import com.example.ourapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    Optional<User> findById(Long id);


}
