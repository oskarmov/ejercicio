package com.demo.users.error;

public class AuthenticationException extends RuntimeException{
  public AuthenticationException(String message) {
    super(message);
  }
}
