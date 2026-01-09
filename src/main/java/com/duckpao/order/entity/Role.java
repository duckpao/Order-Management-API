package com.duckpao.order.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    protected Role() {}

    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
