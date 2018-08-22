package com.accolite.pru.health.AuthApp.advice;

import com.accolite.pru.health.AuthApp.exception.AppException;
import com.accolite.pru.health.AuthApp.exception.BadRequestException;
import com.accolite.pru.health.AuthApp.exception.ResourceAlreadyInUseException;
import com.accolite.pru.health.AuthApp.exception.ResourceNotFoundException;
import com.accolite.pru.health.AuthApp.model.payload.ApiResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestControllerAdvice
public class AuthControllerAdvice {

	private static final Logger logger = Logger.getLogger(AuthControllerAdvice.class);

	@Autowired
	private MessageSource messageSource;

	/**
	 * Process validation error that throw MethodArgumentNotValidException
	 * @param ex the exception
	 * @return the response dto
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ApiResponse processValidationError(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();
		ApiResponse response = new ApiResponse();
		response.setSuccess(false);
		logger.info("Localized validation error: " + ex.getLocalizedMessage());
		response.setData(processFieldErrors(fieldErrors).toString());
		return response;
	}

	/**
	 * Utility Method to generate localized message for a list of field errors
	 * @param fieldErrors the field errors
	 * @return the list
	 */
	private List<String> processFieldErrors(List<FieldError> fieldErrors) {
		return fieldErrors.stream().map(this::resolveLocalizedErrorMessage).collect(Collectors.toList());
	}

	/**
	 * Resolve localized error message. Utiity method to generate a localized error
	 * message
	 * @param fieldError the field error
	 * @return the string
	 */
	private String resolveLocalizedErrorMessage(FieldError fieldError) {
		Locale currentLocale = LocaleContextHolder.getLocale();
		String localizedErrorMessage = messageSource.getMessage(fieldError, currentLocale);
		logger.info(localizedErrorMessage);
		return localizedErrorMessage;
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
