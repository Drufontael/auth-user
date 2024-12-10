package br.dev.drufontael.auth_user.configuration.security.jwt;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
