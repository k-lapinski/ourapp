package com.example.ourapp.controller;

import com.example.ourapp.model.User;
import com.example.ourapp.repository.UserRepository;
import com.example.ourapp.service.ItemService;
import com.example.ourapp.service.ItemServiceImpl;
import com.example.ourapp.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @GetMapping("/user/{id}")
    public String showUser(@PathVariable("id") Long id, Model model) {
        Optional<User> user = userServiceImpl.findUserById(id);
        model.addAttribute("user", user);
        return "user-details";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable(value = "id")  Long id) {
        userServiceImpl.deleteUser(id);
        return "redirect:/";
    }
}
