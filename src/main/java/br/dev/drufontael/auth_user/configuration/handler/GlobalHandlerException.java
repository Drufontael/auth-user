package br.dev.drufontael.auth_user.configuration.handler;

import br.dev.drufontael.auth_user.domain.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalHandlerException {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        String path = request.getRequestURI();
        log.error("Access denied: " ,e);
        return ResponseEntity.status(403).body(ErrorResponse.from(e.getMessage(),403, path));
    }

    @ExceptionHandler(InvalidEmailFormatException.class)
    public ResponseEntity<?> handleInvalidEmailFormatException(InvalidEmailFormatException e, HttpServletRequest request) {
        String path = request.getRequestURI();
        log.error("Invalid email format: ", e);
        return ResponseEntity.status(400).body(ErrorResponse.from(e.getMessage(),400, path));
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<?> handleInvalidRequestException(InvalidRequestException e, HttpServletRequest request) {
        String path = request.getRequestURI();
        log.error("Invalid request: ", e);
        return ResponseEntity.status(400).body(ErrorResponse.from(e.getMessage(),400, path));
    }

    @ExceptionHandler(UserAlreadExistsException.class)
    public ResponseEntity<?> handleUserAlreadExistsException(UserAlreadExistsException e, HttpServletRequest request) {
        String path = request.getRequestURI();
        log.error("User already exists: ", e);
        return ResponseEntity.status(409).body(ErrorResponse.from(e.getMessage(),409, path));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException e, HttpServletRequest request) {
        String path = request.getRequestURI();
        log.error("User not found: ", e);
        return ResponseEntity.status(404).body(ErrorResponse.from(e.getMessage(),404, path));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception e, HttpServletRequest request) {
        String path = request.getRequestURI();
        log.error("Unexpected error occurred: ", e);
        return ResponseEntity.status(500).body(ErrorResponse.from("An internal server error occurred", 500, path));
    }




    @Getter
    private static class ErrorResponse {
        private String error;
        private final int status;
        private final LocalDateTime timestamp;
        private final String path;

        private ErrorResponse(String error, int status, String path) {
            this.error = error;
            this.status = status;
            this.timestamp = LocalDateTime.now();
            this.path = path;
        }

        public static ErrorResponse from(String e, int status, String path) {
            return new ErrorResponse(e, status, path);
        }

    }

}
