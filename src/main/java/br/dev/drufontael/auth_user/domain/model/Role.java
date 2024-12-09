package br.dev.drufontael.auth_user.domain.model;

public enum Role {
    ADMIN("ROLE_ADMIN"), USER("ROLE_USER"), GUEST("ROLE_GUEST");

    public final String role;

    Role(String role) {
        this.role = role;
    }
    public String getRole() {
        return role;
    }

    public int expiration() {
        return switch (this.role) {
            case "ROLE_ADMIN" -> 15;
            case "ROLE_USER" -> 60;
            case "ROLE_GUEST" -> 300;
            default -> 0;
        };
    }

    public int priority() {
        return switch (this.role) {
            case "ROLE_ADMIN" -> 1;
            case "ROLE_USER" -> 2;
            case "ROLE_GUEST" -> 3;
            default -> 0;
        };
    }
}
