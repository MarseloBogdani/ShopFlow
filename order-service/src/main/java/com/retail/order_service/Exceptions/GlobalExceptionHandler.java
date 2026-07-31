package com.retail.order_service.Exceptions;

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
        public ResponseEntity<OrderErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
            return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
        }
        //

    //END 404

    //409

    //END 409
        
    //407
        //Not Enough Stock Exception
        @ExceptionHandler(NotEnoughStockException.class)
        public ResponseEntity<OrderErrorResponse> handleNotEnoughStock(NotEnoughStockException ex, HttpServletRequest request) {
            return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        } 
        //
    //END 407

    //500
        //Handle Generic Exception
        @ExceptionHandler(Exception.class)
        public ResponseEntity<OrderErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
            System.out.println("!! Exception: " + ex);
            return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
        }
        //
    
    //END 500

    //400
        //Handle Jakarta Bean Exception
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<OrderErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        
            String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildResponse(HttpStatus.BAD_REQUEST, errorMessage, request);
        }
        //

        //Handle IllegalArgumentException
        @ExceptionHandler(IllegalArgumentException.class) 
        public ResponseEntity<OrderErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) { 
            return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        }
        //

    //END 400
        

    //MyHelper
    private ResponseEntity<OrderErrorResponse> buildResponse(HttpStatus status, String message, HttpServletRequest request) {
        OrderErrorResponse error = new OrderErrorResponse(
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
