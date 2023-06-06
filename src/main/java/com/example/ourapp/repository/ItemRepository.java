package com.example.ourapp.repository;

import com.example.ourapp.dto.ItemDto;
import com.example.ourapp.model.Item;
import com.example.ourapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findByName(String name);

    @Query("SELECT i FROM Item i WHERE i.ownerMail = :ownerMail")
    List<Item> findMyItemsByMail(@Param("ownerMail") String ownerMail);

    @Query("SELECT i FROM Item i WHERE i.id = :id")
    Item findItemById(@Param("id") Long id);
}
