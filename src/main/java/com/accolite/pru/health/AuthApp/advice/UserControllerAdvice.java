package com.accolite.pru.health.AuthApp.advice;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserControllerAdvice {

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public void handleRuntimeException(){

	}
}
