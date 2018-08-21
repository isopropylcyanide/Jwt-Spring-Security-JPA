package com.accolite.pru.health.AuthApp.advice;

import com.accolite.pru.health.AuthApp.exception.AppException;
import com.accolite.pru.health.AuthApp.exception.BadRequestException;
import com.accolite.pru.health.AuthApp.exception.ResourceAlreadyInUseException;
import com.accolite.pru.health.AuthApp.exception.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserControllerAdvice {

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public void handleRuntimeException() {

	}

	@ExceptionHandler(value = AppException.class)
	public void handleAppException() {

	}

	@ExceptionHandler(value = ResourceAlreadyInUseException.class)
	public void handleResourceAlreadyInUseException() {

	}

	@ExceptionHandler(value = ResourceNotFoundException.class)
	public void handleResourceNotFoundException() {

	}

	@ExceptionHandler(value = BadRequestException.class)
	public void handleBadRequestExceptionException() {

	}

	@ExceptionHandler(value = UsernameNotFoundException.class)
	public void handleUsernameNotFoundExceptionException() {

	}


}
