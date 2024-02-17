package com.cog.hotel.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cog.hotel.model.AuthenticationResponse;

import lombok.extern.slf4j.Slf4j;
import java.util.*;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerAdvice {

	/**
	 * 
	 * @param ex
	 * @return ResponseEntity
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> validationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> resp = new HashMap<>();

		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			log.error(message);
			resp.put(fieldName, message);

		});
		return new ResponseEntity<Map<String, String>>(resp, HttpStatus.FORBIDDEN);
	}

	/**
	 * 
	 * @param e
	 * @return ResponseEntity
	 */
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Object> exception(Exception e) {
		log.error(e.getMessage());
		AuthenticationResponse authenticationResponse = new AuthenticationResponse(0, "Invalid", "Invalid", "Invalid",
				"Invalid", "Invalid", "Invalid", false);
		return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
	}

	@ExceptionHandler(value = CustomerNotFoundException.class)
	@ResponseBody
	public ResponseEntity<?> handleValidationException(CustomerNotFoundException customerNotFoundException) {
		return new ResponseEntity<>(customerNotFoundException.getMessage(), HttpStatus.OK);
	}
}
