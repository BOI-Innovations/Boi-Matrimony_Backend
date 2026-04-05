//package com.matrimony.exception;
//
//import com.matrimony.model.dto.response.ApiResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
//        ApiResponse response = new ApiResponse(false, ex.getMessage());
//        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(UserAlreadyExistsException.class)
//    public ResponseEntity<ApiResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
//        ApiResponse response = new ApiResponse(false, ex.getMessage());
//        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
//    }
//
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException ex) {
//        ApiResponse response = new ApiResponse(false, ex.getMessage());
//        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
//    }
//
//    @ExceptionHandler(CustomValidationException.class)
//    public ResponseEntity<ApiResponse> handleCustomValidationException(CustomValidationException ex) {
//        ApiResponse response = new ApiResponse(false, ex.getMessage());
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse> handleGlobalException(Exception ex) {
//        ApiResponse response = new ApiResponse(false, "An error occurred: " + ex.getMessage());
//        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//}
package com.matrimony.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.matrimony.model.entity.ResponseEntity;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity handleValidationExceptions(MethodArgumentNotValidException ex) {

		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors()
				.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
		logger.warn("Validation failed: {}", errors);
		return new ResponseEntity("Validation failed", 400, errors);
	}

	@ExceptionHandler(GenericException.class)
	public ResponseEntity handleGenericException(GenericException ex) {
		logger.warn("Business exception: {}", ex.getMessage());
		return new ResponseEntity(ex.getMessage(), 403, null);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity handleAllExceptions(Exception ex) {
		logger.error("Unexpected system error", ex);
		return new ResponseEntity("Something went wrong. Please try again later.", 500, null);
	}

	@ExceptionHandler(AuthorizationDeniedException.class)
	public ResponseEntity handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
		logger.warn("Access denied: {}", ex.getMessage());
		return new ResponseEntity("Access Denied. You are not authorized to perform this action.", 403, null);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
		String message = "Invalid value for parameter: " + ex.getName();
		return new ResponseEntity(message, 400, null);
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity handleNoResourceFound(NoResourceFoundException ex) {
	    return new ResponseEntity(
	            "API endpoint not found: " + ex.getResourcePath(),
	            404,
	            null
	    );
	}
}