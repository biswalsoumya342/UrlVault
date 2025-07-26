package com.soumya.urlshortener.exception;

import com.soumya.urlshortener.payload.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handelResourceNotFoundException(ResourceNotFoundException ex){
        return new ResponseEntity<>(
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND.value()
                ),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handelBadRequestException(BadRequestException ex){
        return new ResponseEntity<>(
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        ex.getMessage(),
                        HttpStatus.BAD_REQUEST.value()
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UnauthorizeAccessException.class)
    public ResponseEntity<?> handelUnauthorizeAccessException(UnauthorizeAccessException ex){
        return new ResponseEntity<>(
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        ex.getMessage(),
                        HttpStatus.BAD_REQUEST.value()
                ),
                HttpStatus.BAD_REQUEST
        );
    }
}
