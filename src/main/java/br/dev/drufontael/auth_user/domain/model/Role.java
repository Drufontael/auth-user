package br.dev.drufontael.auth_user.domain.model;

public enum Role {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    GUEST("ROLE_GUEST"),
    SERVICE("ROLE_SERVICE");

    public final String role;

    Role(String role) {
        this.role = role;
    }
    public String getRole() {
        return role;
    }

    public int expiration() {
        return switch (this.role) {
            case "ROLE_ADMIN" -> 45;
            case "ROLE_USER" -> 60;
            case "ROLE_GUEST", "ROLE_SERVICE" -> 300;
            default -> 0;
        };
    }

}
