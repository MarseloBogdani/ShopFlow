package com.retail.catalog_service.catalog_exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {super(message);}
}
