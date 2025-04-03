package br.com.fiap.fin_money_api.exception;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidationHandler {

    record ValidationError (String field, String message){ // campo que deu problema - mensagem do problema
        public ValidationError(FieldError error) {
           this(error.getField(), error.getDefaultMessage());
        }} 

    @ExceptionHandler(exception = MethodArgumentNotValidException.class) // quando falhar uma validação do bean validation
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ValidationError> handler(MethodArgumentNotValidException e){
        return e.getFieldErrors()
                .stream()
                .map(ValidationError :: new) // método de referência - sintaxe :: - função é o construtor
                .toList();
    }

}