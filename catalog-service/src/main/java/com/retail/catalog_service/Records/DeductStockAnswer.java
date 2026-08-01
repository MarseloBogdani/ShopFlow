package com.retail.catalog_service.Records;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Payload for deducting item stock for a specific user.
 */
public record DeductStockAnswer(
    @NotNull(message = "Product ID is required")
    Integer productId,

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity to deduct must be at least 1")
    Integer quantity,

    @NotNull(message = "Price is required")
    @Positive(message = "Item price cannot be negative or zero.")
    BigDecimal price
) {}