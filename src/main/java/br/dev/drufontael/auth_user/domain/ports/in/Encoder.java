package br.dev.drufontael.auth_user.domain.ports.in;

public interface Encoder {
    String encode(String password);
    boolean matches(String password, String encodedPassword);
}
