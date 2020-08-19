package com.example.demo.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintViolation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class ErrorModel {

    private HttpStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    private String message;

    private String debugMessage;

    private ErrorModel() {
        timestamp = LocalDateTime.now();
    }

    ErrorModel(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.message = ex.getMessage();
        this.debugMessage = ex.getLocalizedMessage();
    }

}
