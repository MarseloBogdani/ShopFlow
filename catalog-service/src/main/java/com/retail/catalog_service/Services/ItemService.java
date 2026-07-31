package com.retail.catalog_service.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.retail.catalog_service.Entities.Item;
import com.retail.catalog_service.Exceptions.ConflictException;
import com.retail.catalog_service.Exceptions.ResourceNotFoundException;
import com.retail.catalog_service.Repositories.ItemRepository;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;

    }

    public Page<Item> getAllItems(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    public Page<Item> findByStockBetween(Integer minStock,Integer maxStock,Pageable pageable){
        if(minStock == null || minStock < 0 || maxStock == null || maxStock < 0 || maxStock >2147483647 || minStock > maxStock){
            throw new IllegalArgumentException("Stock range must be a valid range");
        }
        return itemRepository.findByStockBetween(minStock,maxStock,pageable);
    }

    public Page<Item> findByPriceBetween(Integer minPrice, Integer maxPrice, Pageable pageable){
        if(minPrice == null || minPrice < 0 || maxPrice == null || maxPrice < 0 || maxPrice >2147483647 || minPrice > maxPrice){
            throw new IllegalArgumentException("Stock range must be a valid range");
        }
        return itemRepository.findByPriceBetween(minPrice,maxPrice,pageable);
    }

    public Item getItemById(Integer id) {
        if(id == null || id <=0){
            throw new IllegalArgumentException("id must be a positive integer");
        }

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
        if(id == null || id <=0){
            throw new IllegalArgumentException("id must be a positive integer");
        }

        Item my_item = getItemById(id);

        if(newItem.getName() != null){
            my_item.setName(newItem.getName());
        }
        if(newItem.getPrice() != null){
            my_item.setPrice(newItem.getPrice());
        }
        if(newItem.getStock() != null){
            my_item.setStock(newItem.getStock());
        }
        
        return itemRepository.save(my_item);
    }

    @Transactional
    public Boolean deleteItem(Integer id) {
        if(id == null || id <=0){
            throw new IllegalArgumentException("id must be a positive integer");
        }

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
        if(id == null || id <=0){
            throw new IllegalArgumentException("id must be a positive integer");
        }

        Item item = getItemById(id);
        
        if (item.getStock() <= quantityToDeduct) {
            throw new ConflictException("Insufficient stock for item: " + item.getName() + ". Available: " + item.getStock());
        }
        
        item.setStock(item.getStock() - quantityToDeduct);
        return itemRepository.save(item);
    }

    public Integer getStockById(Integer id) {
        if(id == null || id <=0){
            throw new IllegalArgumentException("id must be a positive integer");
        }
        
        return itemRepository
                .findStockById(id);
    }
}
