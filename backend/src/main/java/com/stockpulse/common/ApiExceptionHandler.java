package com.stockpulse.common;

import java.util.NoSuchElementException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class) @ResponseStatus(HttpStatus.NOT_FOUND)
    ProblemDetail notFound(NoSuchElementException e) { return problem(HttpStatus.NOT_FOUND, e.getMessage()); }
    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class}) @ResponseStatus(HttpStatus.BAD_REQUEST)
    ProblemDetail badRequest(Exception e) { return problem(HttpStatus.BAD_REQUEST, e.getMessage()); }
    private ProblemDetail problem(HttpStatus status, String detail) { ProblemDetail p = ProblemDetail.forStatusAndDetail(status, detail == null ? status.getReasonPhrase() : detail); p.setTitle(status.getReasonPhrase()); return p; }
}