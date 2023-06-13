package com.example.ourapp.controller;

import com.example.ourapp.dto.UserDto;
import com.example.ourapp.model.Role;
import com.example.ourapp.model.User;
import com.example.ourapp.repository.UserRepository;
import com.example.ourapp.service.ItemService;
import com.example.ourapp.service.ItemServiceImpl;
import com.example.ourapp.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/{email}")
    public String showUser(@PathVariable("email") String email, Model model) {
        Optional<User> user = Optional.ofNullable(userServiceImpl.findUserByEmail(email));
        model.addAttribute("user", user);
        logger.info("/user/"+email);
        return "user-details";
    }

    @GetMapping("/user/delete/{email}")
    public String deleteUser(@PathVariable(value = "email")  String email) {
        userServiceImpl.deleteUser(email);
        logger.info("/user/delete/"+email);
        return "redirect:/users";
    }

    @GetMapping("/user/edit/{email}")
    public String showEditUserForm(@PathVariable("email") String email, Model model){
        User user = userServiceImpl.findUserByEmail(email);
        UserDto userDto = userServiceImpl.mapToUserDto(user);
        model.addAttribute("user", userDto);
        logger.info("/user/edit/"+email);
        return "edit-user-form";
    }

    @PostMapping("/user/edit/save")
    public String editUser(@Valid @ModelAttribute("user") UserDto userDto,
                           BindingResult result,
                           Model model,@RequestParam("role") String role){
        userDto.setFirstName(userDto.getFirstName().substring(0, 1).toUpperCase()+userDto.getFirstName().substring(1));
        userDto.setLastName(userDto.getLastName().substring(0, 1).toUpperCase()+userDto.getLastName().substring(1));
        userServiceImpl.editUser(userDto, role);

        logger.info("/user/edit/save");
        return "redirect:/users?success";
    }

    @GetMapping("/mydata")
    public String showMyDataForm(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userServiceImpl.findUserByEmail(username);
        UserDto userDto = userServiceImpl.mapToUserDto(user);
        Role roles = user.getRoles().get(0);
        String role = roles.getName();
        model.addAttribute("user", userDto);
        model.addAttribute("role", role);
        logger.info("/mydata");

        return "my-data-form";
    }

    @PostMapping("/mydata/saveme")
    public String editMe(@Valid @ModelAttribute("user") UserDto userDto,
                           BindingResult result,
                           Model model, @ModelAttribute("role") String role){
        userDto.setFirstName(userDto.getFirstName().substring(0, 1).toUpperCase()+userDto.getFirstName().substring(1));
        userDto.setLastName(userDto.getLastName().substring(0, 1).toUpperCase()+userDto.getLastName().substring(1));
        userServiceImpl.editUser(userDto, role);

        logger.info("/mydata/saveme");
        return "redirect:/mydata?success";
    }

    @GetMapping("/users/cancel")
    public String cancelUser(){
        return "/users";
    }
}
