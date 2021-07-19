package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler({CustomAuthenticationException.class})
    public ResponseEntity<ErrorModel> handleAuthenticationException(CustomAuthenticationException e) {
        return buildResponseEntity(new ErrorModel(HttpStatus.FORBIDDEN, e));
    }

    @ExceptionHandler({DataNotFoundException.class})
    public ResponseEntity<ErrorModel> handleDataNotFoundException(DataNotFoundException e) {
        return buildResponseEntity(new ErrorModel(HttpStatus.NOT_FOUND, e));
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ErrorModel> handleConstraintViolationException(ConstraintViolationException e) {
        return buildResponseEntity(new ErrorModel(HttpStatus.BAD_REQUEST, e));
    }

    @ExceptionHandler({AlreadySharedException.class})
    public ResponseEntity<ErrorModel> handleAlreadySharedException(AlreadySharedException e) {
        return buildResponseEntity((new ErrorModel(HttpStatus.BAD_REQUEST, e)));
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<ErrorModel> handleValidationException(ValidationException e) {
        return buildResponseEntity(new ErrorModel(HttpStatus.BAD_REQUEST, e));
    }

    private ResponseEntity<ErrorModel> buildResponseEntity(ErrorModel errorModel) {
        return new ResponseEntity<>(errorModel, errorModel.getStatus());
    }

}
