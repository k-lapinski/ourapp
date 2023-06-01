package com.example.ourapp.service;

import com.example.ourapp.dto.ItemDto;
import com.example.ourapp.dto.UserDto;
import com.example.ourapp.model.Item;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public interface ItemService {
    void saveItem(ItemDto itemDto);

    void deleteItem(Long id);

    List<ItemDto> findAllItems();
}
