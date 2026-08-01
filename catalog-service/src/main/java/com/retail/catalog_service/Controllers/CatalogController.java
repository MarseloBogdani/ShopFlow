package com.retail.catalog_service.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.retail.catalog_service.Entities.Item;
import com.retail.catalog_service.Records.DeductStockAnswer;
import com.retail.catalog_service.Records.DeductStockRequest;
import com.retail.catalog_service.Services.ItemService;

import jakarta.validation.Valid;

@RestController
public class CatalogController {
    private final ItemService itemService;

    @Autowired
    public CatalogController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public Page<Item> getCatalog(@PageableDefault(size = 20) Pageable pageable) {
        return itemService.getAllItems(pageable);
    }

    @GetMapping("/items/by-stock-range")
    public Page<Item> getByStockRange(
            @RequestParam(defaultValue = "0") Integer minStock,
            @RequestParam(defaultValue = "2147483647") Integer maxStock,
            @PageableDefault(size = 20) Pageable pageable) {
        
        return itemService.findByStockBetween(minStock, maxStock, pageable);
    }

    @GetMapping("items/by-price-range")
    public  Page<Item> findByPriceRange(
        @RequestParam(defaultValue = "0") Integer minPrice,
        @RequestParam(defaultValue = "2147483647") Integer maxPrice,
        @PageableDefault(size = 20) Pageable pageable) {
    
    return itemService.findByPriceBetween(minPrice, maxPrice, pageable);
    }

    @GetMapping("/items/{id}")
    public Item getItem(@PathVariable Integer id) {
        return itemService.getItemById(id);
    }

    @GetMapping("/items/Stock/{id}")
    public Item getStockById(@PathVariable Integer id) {
        return itemService.getItemById(id);
    }

    @PostMapping("/items")
    public Item addItem(@Valid @RequestBody Item newItem) {
        return itemService.addItem(newItem);
    }

    @PostMapping("/items/deduct-batch")
    public ResponseEntity<List<DeductStockAnswer>> deductStockBatch(@RequestBody List<@Valid DeductStockRequest> requests) {
        List<DeductStockAnswer> awnser_obj = itemService.reserveStock(requests);

        return ResponseEntity.ok(awnser_obj);
    }

    @PostMapping("/items/{id}/deduct")
    public ResponseEntity<Item> deductStock(@PathVariable Integer id, @RequestParam Integer quantity) {
        Item updatedItem = itemService.deductStock(id, quantity);
        return ResponseEntity.ok(updatedItem);
    }

    @PutMapping("/items/update/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Integer id, @Valid @RequestBody Item newItem) {
        Item savedItem = itemService.updateItem(id, newItem);
        return ResponseEntity.ok(savedItem);
    }

    @DeleteMapping("/items/delete/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Integer id) {
        if (itemService.deleteItem(id)) {
            return ResponseEntity.ok("");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Item with id: " + id + " not found");
        }
    }
}
