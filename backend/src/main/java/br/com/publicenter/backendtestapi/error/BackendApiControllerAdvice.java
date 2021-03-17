package br.com.publicenter.backendtestapi.error;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.AuthenticationException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class BackendApiControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorModel> entityNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        var modelException = new ErrorModel();
        modelException.setTimestamp(Instant.now());
        modelException.setStatus(HttpStatus.NOT_FOUND.value());
        modelException.setError("Msg de ERRO - entityNotFound");
        modelException.setMessage(ex.getMessage());
        modelException.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(modelException);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorModel> dataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        var modelException = new ErrorModel();
        modelException.setTimestamp(Instant.now());
        modelException.setStatus(HttpStatus.BAD_REQUEST.value());
        modelException.setError("Msg de ERRO - dataIntegrityViolation");
        modelException.setMessage(ex.getMessage());
        modelException.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(modelException);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorModel> authenticationException(AuthenticationException ex, HttpServletRequest request) {
        var modelException = new ErrorModel();
        modelException.setTimestamp(Instant.now());
        modelException.setStatus(HttpStatus.UNAUTHORIZED.value());
        modelException.setError("Msg de ERRO - authenticationException");
        modelException.setMessage(ex.getMessage());
        modelException.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(modelException);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        var modelException = new ErrorModel();
        modelException.setTimestamp(Instant.now());
        modelException.setStatus(HttpStatus.BAD_REQUEST.value());
        modelException.setError("Msg de ERRO - authenticationException");
        modelException.setMessage(ex.getMessage());
        modelException.setPath(request.getContextPath());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(modelException);
    }

}
