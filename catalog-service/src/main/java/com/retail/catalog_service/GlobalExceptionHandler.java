package com.retail.catalog_service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;


import com.retail.catalog_service.catalog_exceptions.ConflictException;
import com.retail.catalog_service.catalog_exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //404
        //Not Found Exception
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<Catalog_error_response> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
            return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
        }

    //409   
        //Conflict Exception
        @ExceptionHandler(ConflictException.class)
        public ResponseEntity<Catalog_error_response> handleConflict(ConflictException ex, HttpServletRequest request) {
            return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
        }

    //500
        //Handle Generic Exception
        @ExceptionHandler(Exception.class)
        public ResponseEntity<Catalog_error_response> handleGenericException(Exception ex, HttpServletRequest request) {
            System.out.println("!! Exception: " + ex);
            return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
        }

    //400
        //Handle Jakarta Bean Exception
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Catalog_error_response> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        
            String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildResponse(HttpStatus.BAD_REQUEST, errorMessage, request);
        }

        //String in Price Exception
        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<Catalog_error_response> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
            
            String errorMessage = "Invalid input format. Please check your data types.";


            if (ex.getMessage() != null && ex.getMessage().contains("java.math.BigDecimal")) {
                errorMessage = "The price field must be a valid number (e.g., 10.99). Letters are not allowed.";
            }

            return buildResponse(HttpStatus.BAD_REQUEST, errorMessage, request);
        }
        
    //MyHelper
    private ResponseEntity<Catalog_error_response> buildResponse(HttpStatus status, String message, HttpServletRequest request) {
        Catalog_error_response error = new Catalog_error_response(
            LocalDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            message,
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, status);
    }
}