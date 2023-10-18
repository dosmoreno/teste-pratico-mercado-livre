package br.com.testepratico.springbootapi.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ControlAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        String erros = "";

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {

            erros += fieldError.getDefaultMessage() + "\n";

        }

        return erros;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleConstraintViolationException(ConstraintViolationException ex) {

        String erros = "";

        for (ConstraintViolation<?> constraintViolation: ex.getConstraintViolations()) {

            erros += constraintViolation.getMessageTemplate() + "\n"; 

        }

        return erros;
    }
}
