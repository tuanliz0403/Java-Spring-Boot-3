package com.tuan.identity_service.exception;

import com.tuan.identity_service.dto.request.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException e){
        ApiResponse<String> response = new ApiResponse<>();
        response.setCode(ErrorCode.UNKNOWN_ERROR.getCode());
        response.setMessage(ErrorCode.UNKNOWN_ERROR.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(AppException e){
        ApiResponse<String> response = new ApiResponse<>();
        response.setCode(e.getError().getCode());
        response.setMessage(e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingMethodArgumentNotValidException(MethodArgumentNotValidException e){
        String enumKey = e.getFieldError().getDefaultMessage();
        ErrorCode error = ErrorCode.valueOf(enumKey);
        ApiResponse<String> response = new ApiResponse<>();

        response.setCode(error.getCode());
        response.setMessage(error.getMessage());

        return ResponseEntity.badRequest().body(response);
    }
}
