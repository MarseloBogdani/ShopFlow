package com.retail.order_service.Records;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Payload for deducting item stock for a specific user.
 */
public record DeductStockRequest(
    @NotNull(message = "Product ID is required")
    Integer productId,

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity to deduct must be at least 1")
    Integer quantity
) {}