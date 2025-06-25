package com.group_7.backend.exception;

import com.group_7.backend.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //Lỗi validation cho các dữ liệu input
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleBadRequestException(MethodArgumentNotValidException exception) {
        String responseMessage = "";
        for (FieldError fieldError : exception.getFieldErrors()) {
            responseMessage += fieldError.getDefaultMessage() + "\n";
        }
        return new ResponseEntity(responseMessage, HttpStatus.BAD_REQUEST);
    }
    //Lỗi các thực thể ko tìm thấy trong data
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity handleResourceNotFoundException(ResourceNotFoundException exception) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
    //Lỗi liên quan tới SQL: duplicate data...
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentException(IllegalArgumentException exception) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
    //Lỗi data type của request/input gửi
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        //System.out.println( exception.getMostSpecificCause().getMessage() );//Thông báo lỗi chi tiết
        String message = "Invalid request: Please check your input data types and format."; //Thông báo custom
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
    //Lỗi quyền truy cập ko hợp lệ
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity(ex.getMessage(), HttpStatus.FORBIDDEN);
    }
    //Các lỗi khác...
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        return new ResponseEntity("Internal server error: "+ ex.getMessage() + ". Please contact admin.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
