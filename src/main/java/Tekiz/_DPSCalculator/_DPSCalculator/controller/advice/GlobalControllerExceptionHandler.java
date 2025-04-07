package Tekiz._DPSCalculator._DPSCalculator.controller.advice;

import Tekiz._DPSCalculator._DPSCalculator.model.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler
{
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> handleNotFoundException(ResourceNotFoundException resourceNotFoundException) {
		return ResponseEntity
			.status(HttpStatus.NOT_FOUND)
			.body(resourceNotFoundException.getMessage());
	}
}
