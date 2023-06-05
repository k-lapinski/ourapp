package com.example.ourapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=false)
    private String name;

    @Column(nullable=false, unique=false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable=false, unique=false)
    private String ownerMail;
}
