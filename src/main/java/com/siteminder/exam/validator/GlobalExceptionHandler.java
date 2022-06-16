package com.siteminder.exam.validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.siteminder.exam.exception.EmailSendingException;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@ControllerAdvice
public class GlobalExceptionHandler {

	@Builder
	@Setter
	@Getter
	public static class ExceptionDetails {
		private long timestamp;
        private HttpStatus status;
        private String error;
        private Class<? extends Throwable> exception;
        private String path;
        
        @JsonInclude(value = Include.NON_EMPTY)
        private Map<String, String> fieldErrors;
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionDetails badRequest(MethodArgumentNotValidException exception, HttpServletRequest req,
        HttpServletResponse res) {
		
        return ExceptionDetails.builder()
        		.timestamp(System.currentTimeMillis())
        		.exception(exception.getClass())
        		.status(HttpStatus.BAD_REQUEST)
        		.fieldErrors(getFieldErrorMessages(exception.getBindingResult().getFieldErrors()))
        		.path(req.getRequestURI())
        		.error(exception.getMessage())
        		.build();
    }
	
	@ExceptionHandler(EmailSendingException.class)
    @ResponseBody
    public ResponseEntity<?> badRequest(EmailSendingException exception, HttpServletRequest req,
        HttpServletResponse res) {
		
		ExceptionDetails exceptionDetails = ExceptionDetails.builder()
        		.timestamp(System.currentTimeMillis())
        		.exception(exception.getClass())
        		.status(exception.getHttpStatus())
        		.path(req.getRequestURI())
        		.error(exception.getMessage())
        		.build();
		
        return ResponseEntity.status(exception.getHttpStatus().value()).body(exceptionDetails);
    }
	
	private Map<String, String> getFieldErrorMessages(List<FieldError> fieldErrors) {
		Map<String, String> messages = new HashMap<>();
        fieldErrors.stream()
            .forEach(e -> messages.put(e.getField(), e.getDefaultMessage()));
        return messages;
    }
}
