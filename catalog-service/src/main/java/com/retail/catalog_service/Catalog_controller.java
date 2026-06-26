package com.retail.catalog_service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;


@RestController
public class Catalog_controller {
    private final ItemService itemService;

    @Autowired
    public Catalog_controller(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public List<Item> getCatalog() {
        return itemService.getAllItems();
    }

    @GetMapping("/items/{id}")
    public Item getItem(@PathVariable Integer id) {
        return itemService.getItemById(id);
    }

    @PostMapping("/items") 
    public Item addItem(@Valid @RequestBody Item newItem){
        return itemService.addItem(newItem);
    }

    @PutMapping("/items/update/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Integer id,@Valid @RequestBody Item newItem){
        Item savedItem = itemService.updateItem(id,newItem);
        return ResponseEntity.ok(savedItem); 
    }

    @DeleteMapping("/items/delete/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Integer id){
        if(itemService.deleteItem(id)) {
            return ResponseEntity.ok(""); //200
        } 
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item with id: " + id + " not found"); // 404
        }

    }

}
