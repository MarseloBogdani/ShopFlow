package com.retail.catalog_service.Seed;
/*

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeedDataConfig {

    private static final Logger log = LoggerFactory.getLogger(SeedDataConfig.class);

    @Bean
    CommandLineRunner initDatabase(ItemRepository itemRepository) {
        return args -> {
            // 1. Check if data already exists to avoid duplicate seeding on every restart
            if (itemRepository.count() > 0) {
                log.info("Database already contains data. Skipping seeding.");
                return;
            }

            log.info("Starting database seeding...");

            // 2. Define how many items you want to seed (e.g., 10,000 items)
            int totalItems = 10000; 

            // 3. Generate data efficiently using streams
            List<Item> mockItems = IntStream.rangeClosed(1, totalItems)
                .mapToObj(i -> {
                    Item item = new Item();
                    item.setName("Item Product " + i);
                    item.setPrice(BigDecimal.valueOf(10.0 + (i * 0.5))); 
                    item.setStock(i % 50 + 5); // Generates a stock between 5 and 54
                    return item;
                })
                .collect(Collectors.toList());

            // 4. Batch save to the database
            log.info("Saving {} items to the database...", totalItems);
            itemRepository.saveAll(mockItems);

            log.info("Database seeding completed successfully!");
        };
    }
}
*/
