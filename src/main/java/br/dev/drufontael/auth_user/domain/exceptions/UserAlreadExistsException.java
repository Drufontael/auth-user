package br.dev.drufontael.auth_user.domain.exceptions;

public class UserAlreadExistsException extends RuntimeException {
    public UserAlreadExistsException(String message) {
        super(message);
    }
}
