package com.example.ourapp.controller;

import com.example.ourapp.dto.ItemDto;
import com.example.ourapp.dto.UserDto;
import com.example.ourapp.model.Item;
import com.example.ourapp.model.User;
import com.example.ourapp.repository.ItemRepository;
import com.example.ourapp.service.ItemService;
import com.example.ourapp.service.ItemServiceImpl;
import com.example.ourapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserService userService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("items/delete/{id}")
    public String deleteItem(@PathVariable(value = "id") Long id) {
        itemService.deleteItem(id);
        return "redirect:/myitems";
    }

    @GetMapping("/items")
    public String showCreateForm(Model model) {
        model.addAttribute("item", new ItemDto());
        return "create-item";
    }

    @PostMapping("/items/save")
    public String createItem(@ModelAttribute("item") ItemDto item) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByEmail(username);
        item.setOwnerMail(user.getEmail());
        itemService.saveItem(item);
        return "redirect:/myitems";
    }

    @GetMapping("/myitems")
    public String showMyItems(Model model){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ItemDto> items = itemService.findMyItems(username);
        model.addAttribute("items", items);
        return "my-items";
    }
}
