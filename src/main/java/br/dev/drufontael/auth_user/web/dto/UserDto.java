package br.dev.drufontael.auth_user.web.dto;

import br.dev.drufontael.auth_user.domain.model.User;

public record UserDto(String username, String email, String password, String role) {
    public User toDomain() {
        return User.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();
    }
}
