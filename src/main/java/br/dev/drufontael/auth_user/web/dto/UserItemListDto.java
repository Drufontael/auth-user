package br.dev.drufontael.auth_user.web.dto;

import br.dev.drufontael.auth_user.domain.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserItemListDto(UUID id, String username, String email,int totalLogins, LocalDateTime lastLogin) {
    public static UserItemListDto fromUser(User user) {
        int totalLogins = user.getLogins().size();
        LocalDateTime lastLogin = user.getLogins().isEmpty() ? null : user.getLogins().get(user.getLogins().size() - 1);
        return new UserItemListDto(user.getId(), user.getUsername(), user.getEmail(), totalLogins, lastLogin);
    }
}
