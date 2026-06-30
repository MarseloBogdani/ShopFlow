package com.retail.catalog_service.Exceptions;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //404
        //Not Found Exception
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<CatalogErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
            return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
        }
        //

    //END 404

    //409   
        //Conflict Exception
        @ExceptionHandler(ConflictException.class)
        public ResponseEntity<CatalogErrorResponse> handleConflict(ConflictException ex, HttpServletRequest request) {
            return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
        }
        //

    //END 409

    //500
        //Handle Generic Exception
        @ExceptionHandler(Exception.class)
        public ResponseEntity<CatalogErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
            System.out.println("!! Exception: " + ex);
            return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
        }
        //
    
    //END 500

    //400
        //Handle Jakarta Bean Exception
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<CatalogErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        
            String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildResponse(HttpStatus.BAD_REQUEST, errorMessage, request);
        }
        //

        //String in Price Exception
        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<CatalogErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
            
            String errorMessage = "Invalid input format. Please check your data types.";


            if (ex.getMessage() != null && ex.getMessage().contains("java.math.BigDecimal")) {
                errorMessage = "The price field must be a valid number (e.g., 10.99). Letters are not allowed.";
            }

            return buildResponse(HttpStatus.BAD_REQUEST, errorMessage, request);
        }
        //

        //Handle IllegalArgumentException
        @ExceptionHandler(IllegalArgumentException.class) 
        public ResponseEntity<CatalogErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) { 
            return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        }
        //

    //END 400
        


    //MyHelper
    private ResponseEntity<CatalogErrorResponse> buildResponse(HttpStatus status, String message, HttpServletRequest request) {
        CatalogErrorResponse error = new CatalogErrorResponse(
            LocalDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            message,
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, status);
    }
    //end Myhelper
}