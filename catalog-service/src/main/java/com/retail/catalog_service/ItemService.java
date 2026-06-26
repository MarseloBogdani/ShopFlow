package com.retail.catalog_service;

import java.util.InputMismatchException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.retail.catalog_service.catalog_exceptions.ConflictException;
import com.retail.catalog_service.catalog_exceptions.ResourceNotFoundException;

@Service
public class ItemService {;
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

}
