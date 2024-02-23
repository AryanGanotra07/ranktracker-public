package com.codingee.ranked.ranktracker.util;

import com.codingee.ranked.ranktracker.util.exceptions.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.constraints.Null;
import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
@RestController
public class CustomExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UniqueConstraintException.class)
    public final ResponseEntity<BaseResponse<String>> handleUniqueConstraintException(UniqueConstraintException ex, WebRequest request) {
        return new ResponseEntity<>(BaseResponse.conflict(ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<BaseResponse<String>> handleNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(BaseResponse.notFound(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public final ResponseEntity<BaseResponse<String>> handleValidationException(ValidationException ex, WebRequest request) {
        return new ResponseEntity<>(BaseResponse.badRequest(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public final ResponseEntity<BaseResponse<Null>> handleUnauthorizedException(UnauthenticatedException ex, WebRequest request) {
        return new ResponseEntity<>(BaseResponse.unauthorized(ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public final ResponseEntity<BaseResponse<Null>> handleForbiddenException(ForbiddenException ex, WebRequest request) {
        return new ResponseEntity<>(BaseResponse.forbidden(ex.getMessage()), HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<BaseResponse<String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
       return new ResponseEntity<>(BaseResponse.badRequest(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }



    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> map = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) ->{
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            map.put(fieldName, message);
        });
        BaseResponse<String> response = BaseResponse.badRequest("Invalid Fields");
        response.setMetadata(map);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
