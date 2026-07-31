package com.retail.order_service.Exceptions;

public class NotEnoughStockException extends RuntimeException {
    public NotEnoughStockException(String message) {super(message);}
}
