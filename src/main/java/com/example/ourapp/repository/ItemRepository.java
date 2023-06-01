package com.example.ourapp.repository;

import com.example.ourapp.model.Item;
import com.example.ourapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findByName(String name);

}
