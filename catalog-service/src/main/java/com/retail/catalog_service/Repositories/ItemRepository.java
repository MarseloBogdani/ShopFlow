package com.retail.catalog_service.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.retail.catalog_service.Entities.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer>,JpaSpecificationExecutor<Item> {
    boolean existsByName(String name);

    Page<Item> findByStockBetween(Integer minStock, Integer maxStock, Pageable pageable);
    Page<Item> findByPriceBetween(Integer minPrice, Integer maxPrice, Pageable pageable);
}
