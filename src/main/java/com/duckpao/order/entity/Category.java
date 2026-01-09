package com.duckpao.order.entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    private Boolean active;


    @ManyToMany(mappedBy = "categories")
    private Set<Product> products = new HashSet<>();

    // ===== Constructor cho JPA =====
    protected Category() {
    }

    // ===== Constructor cho business =====
    public Category(String name, String description) {
        this.name = name;
        this.description = description;
        this.active = true;
    }

    // ===== Business methods =====
    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    // ===== Getters =====
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean isActive() {
        return active;
    }
}
