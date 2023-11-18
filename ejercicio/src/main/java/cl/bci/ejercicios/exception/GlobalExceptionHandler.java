package com.demo.users.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(UserEmailFoundException.class)
  public ResponseEntity<Map<String, Object>> emailFoundException(UserEmailFoundException exception){
    Map<String, Object> error = new HashMap<>();
    error.put("mensaje", exception.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(UserException.class)
  public ResponseEntity<Map<String, Object>> userException(UserException exception){
    Map<String, Object> error = new HashMap<>();
    error.put("mensaje", exception.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<Map<String, Object>> userNotFoundException(UserNotFoundException exception){
    Map<String, Object> error = new HashMap<>();
    error.put("mensaje", exception.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(PhoneException.class)
  public ResponseEntity<Map<String, Object>> phoneException(PhoneException exception){
    Map<String, Object> error = new HashMap<>();
    error.put("mensaje", exception.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<Map<String, Object>> authenticationException(AuthenticationException exception){
    Map<String, Object> error = new HashMap<>();
    error.put("mensaje", exception.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(UniqueConstraintViolationException.class)
  public ResponseEntity<Map<String, Object>> handleUniqueConstraintViolationException(UniqueConstraintViolationException exception) {
    Map<String, Object> error = new HashMap<>();
    error.put("mensaje", exception.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    Map<String,Object> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error ->
      errors.put(error.getField(), error.getDefaultMessage())
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }
}
