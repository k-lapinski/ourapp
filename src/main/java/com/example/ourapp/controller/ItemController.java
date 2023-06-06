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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemServiceImpl itemServiceImpl;

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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);
        model.addAttribute("item", new ItemDto());
        return "create-item";
    }

    @PostMapping("/items/save")
    public String createItem(@ModelAttribute("item") ItemDto item) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByEmail(username);
        item.setCategory(item.getCategory().toLowerCase());
        item.setOwnerMail(user.getEmail());
        item.setDate(LocalDate.now());
        item.setSharedInfo(false);
        if(item.getLink()==null) {
            item.setLink("");
        }
        itemService.saveItem(item);
        return "redirect:/myitems";
    }

    @GetMapping("/myitems")
    public String showMyItems(Model model){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ItemDto> items = itemService.findMyItems(username);
        model.addAttribute("items", items);
        model.addAttribute("username", username);
        return "my-items";
    }

    @GetMapping("/allitems")
    public String showAllItems(Model model, @RequestParam(defaultValue = "") String sortBy){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ItemDto> items = itemService.findAllItems();
        if("datesort".equals(sortBy)) {
            items.sort(Comparator.comparing(ItemDto::getDate));
        }
        else if("categorysort".equals(sortBy)) {
            items.sort(Comparator.comparing(ItemDto::getCategory));
        }
        List<ItemDto> sharedItems = new ArrayList<>();
        for(ItemDto itm: items) {
            if(itm.getSharedInfo()) {
                sharedItems.add(itm);
            }
        }
        model.addAttribute("items", sharedItems);
        model.addAttribute("username", username);
        return "all-items";
    }

    @GetMapping("/items/share/{id}")
    public String shareItem(@PathVariable(value = "id") Long id) {
        Item item = itemRepository.findItemById(id);
            ItemDto itemDto = itemServiceImpl.mapToItemDto(item);
            itemDto.setId(id);
        itemDto.setSharedInfo(true);
            itemService.saveItem(itemDto);
        return "redirect:/myitems";
    }
}
