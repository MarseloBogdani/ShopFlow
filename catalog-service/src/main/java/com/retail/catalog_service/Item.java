package com.retail.catalog_service;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


@Entity 
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Size(min = 1,message = "Id must be higher than 1 integer.")
    private Integer id;

    @NotBlank(message = "Item name cannot be empty")
    @Size(min = 0,max = 50,message = "Name must not exeed 50 characters.")
    private String name;

    @NotNull(message = "Price is required")
    @Positive(message = "Item price cannot be negative or zero.")
    private BigDecimal price;

    public Item() {
    }

    public Item(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + "Price: " + price;
    }

}
