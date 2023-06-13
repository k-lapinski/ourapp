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
import java.util.*;
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
    public String showMyItems(
            Model model,
            @RequestParam(value = "sortField", defaultValue = "alpha") String sortField,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection,
            @RequestParam(value = "filterType", required = false) String filterType
    ){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ItemDto> items = itemService.findMyItems(username);

        if (filterType != null) {
            if("category".equals(filterType)){
                Map<String, Integer> categoryMap = new HashMap<>();

                for(ItemDto item : items){
                    String category = item.getCategory();
                    if(categoryMap.containsKey(category)){
                        categoryMap.put(category, categoryMap.get(category) + 1);
                    }
                    else{
                        categoryMap.put(category, 1);
                    }
                }

                List<String> mostPopularCategories = new ArrayList<>();
                int maxCount = 0;
                for(Map.Entry<String, Integer> entry : categoryMap.entrySet()){
                    if(entry.getValue() > maxCount){
                        mostPopularCategories.clear();
                        mostPopularCategories.add(entry.getKey());
                        maxCount = entry.getValue();
                    }
                    else if(entry.getValue() == maxCount){
                        mostPopularCategories.add(entry.getKey());
                    }
                }

                items = items.stream()
                        .filter(i -> mostPopularCategories.contains(i.getCategory()))
                        .collect(Collectors.toList());
            }
            else if("date".equals(filterType)){
                items = items.stream()
                        .filter(i -> i.getDate().isEqual(LocalDate.now()))
                        .collect(Collectors.toList());
            }

        }

        if("asc".equals(sortDirection)){
            if("category".equals(sortField)){
                items.sort(Comparator.comparing(ItemDto::getCategory));
            }
            else if("date".equals(sortField)){
                items.sort(Comparator.comparing(ItemDto::getDate));
            }
            else if("alpha".equals(sortField)){
                items.sort(Comparator.comparing(ItemDto::getName));
            }
        }
        else if("desc".equals(sortDirection)){
            if("category".equals(sortField)){
                items.sort(Comparator.comparing(ItemDto::getCategory).reversed());
            }
            else if("date".equals(sortField)){
                items.sort(Comparator.comparing(ItemDto::getDate).reversed());
            }
            else if("alpha".equals(sortField)){
                items.sort(Comparator.comparing(ItemDto::getName).reversed());
            }
        }
        model.addAttribute("items", items);
        model.addAttribute("username", username);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("filterType", filterType);
        return "my-items";
    }

    @GetMapping("/allitems")
    public String showAllItems(
            Model model,
            @RequestParam(defaultValue = "") String sortBy,
            @RequestParam(value = "filterType", required = false) String filterType
    ){
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

        if (filterType != null) {
            if("catergory".equals(filterType)){
                Map<String, Integer> categoryMap = new HashMap<>();

                for(ItemDto item : sharedItems){
                    String category = item.getCategory();
                    if(categoryMap.containsKey(category)){
                        categoryMap.put(category, categoryMap.get(category) + 1);
                    }
                    else{
                        categoryMap.put(category, 1);
                    }
                }

                List<String> mostPopularCategories = new ArrayList<>();
                int maxCount = 0;
                for(Map.Entry<String, Integer> entry : categoryMap.entrySet()){
                    if(entry.getValue() > maxCount){
                        mostPopularCategories.clear();
                        mostPopularCategories.add(entry.getKey());
                        maxCount = entry.getValue();
                    }
                    else if(entry.getValue() == maxCount){
                        mostPopularCategories.add(entry.getKey());
                    }
                }

                sharedItems = sharedItems.stream()
                        .filter(i -> mostPopularCategories.contains(i.getCategory()))
                        .collect(Collectors.toList());
            }
            else if("date".equals(filterType)){
                sharedItems = sharedItems.stream()
                        .filter(i -> i.getDate().isEqual(LocalDate.now()))
                        .collect(Collectors.toList());
            }
        }

        model.addAttribute("items", sharedItems);
        model.addAttribute("username", username);
        model.addAttribute("filterType", filterType);
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

    @GetMapping("/items/edit/{id}")
    public String showEditItemForm(@PathVariable("id") Long id, Model model){
        Item item = itemRepository.findItemById(id);
        ItemDto itemDto = itemServiceImpl.mapToItemDto(item);
        itemDto.setDate(item.getDate());
        model.addAttribute("item", itemDto);

        return "edit-item-form";
    }

    @PostMapping("/items/edit/save")
    public String editUser(@ModelAttribute("item") ItemDto itemDto,
                           BindingResult result,
                           Model model){
        itemServiceImpl.saveItem(itemDto);

        return "redirect:/myitems?success";
    }

    @GetMapping("/items/cancel")
    public String cancelItem(){
        return "/myitems";
    }
}
