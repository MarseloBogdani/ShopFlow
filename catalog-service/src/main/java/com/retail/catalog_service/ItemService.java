package com.retail.catalog_service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.retail.catalog_service.catalog_exceptions.ConflictException;
import com.retail.catalog_service.catalog_exceptions.ResourceNotFoundException;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;

    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item getItemById(Integer id) {
        return  itemRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item with id: " + id + "not found!"));
    }

    public Item addItem(Item newItem) {
        boolean exists = itemRepository.existsByName(newItem.getName());
        if(exists){
            throw new ConflictException("Item with name: " + newItem.getName() + " already exists!");
        }

        return itemRepository.save(newItem);
    }

    @Transactional
    public Item updateItem(Integer id,Item newItem) {
        Item my_item = getItemById(id);

        my_item.setName(newItem.getName());
        my_item.setPrice(newItem.getPrice());
        my_item.setStock(newItem.getStock());
        
        return itemRepository.save(my_item);
    }

    @Transactional
    public Boolean deleteItem(Integer id) {
        try {
            itemRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Transactional
    public Item deductStock(Integer id, Integer quantityToDeduct) {
        if (quantityToDeduct == null || quantityToDeduct <= 0) {
            throw new IllegalArgumentException("Deduct quantity must be a positive integer");
        }

        Item item = getItemById(id);
        
        if (item.getStock() <= quantityToDeduct) {
            throw new ConflictException("Insufficient stock for item: " + item.getName() + ". Available: " + item.getStock());
        }
        
        item.setStock(item.getStock() - quantityToDeduct);
        return itemRepository.save(item);
    }
}
