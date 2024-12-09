package br.dev.drufontael.auth_user.domain.exceptions;

public class InvalidEmailFormatException extends RuntimeException {

    public InvalidEmailFormatException(String message) {
        super(message);
    }
}
