package com.example.ourapp.service;

import com.example.ourapp.dto.ItemDto;
import com.example.ourapp.dto.UserDto;
import com.example.ourapp.model.Item;
import com.example.ourapp.model.User;
import com.example.ourapp.repository.ItemRepository;
import com.example.ourapp.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private ItemRepository itemRepository;
    private UserRepository userRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public void saveItem(ItemDto itemDto) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setOwnerMail(itemDto.getOwnerMail());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username);
        item.setUser(user);
        itemRepository.save(item);
    }

    @Override
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    private ItemDto mapToItemDto(Item item){
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        return itemDto;
    }

    @Override
    public List<ItemDto> findAllItems() {
        List<Item> items = itemRepository.findAll();
        return items.stream()
                .map((item) -> mapToItemDto(item))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findMyItems(String mail) {
        List<Item> items = itemRepository.findMyItemsByMail(mail);
        return items.stream()
                .map((item) -> mapToItemDto(item))
                .collect(Collectors.toList());
    }
}
