package br.dev.drufontael.auth_user.web.dto;

import br.dev.drufontael.auth_user.domain.model.Role;
import br.dev.drufontael.auth_user.domain.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record UserDetailsDto(UUID id, String username, String email,LocalDateTime createdAt, Set<Role> roles, List<LocalDateTime> logins) {
    public static UserDetailsDto fromUser(User user) {
        return new UserDetailsDto(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt(), user.getRoles(), user.getLogins());
    }
}
