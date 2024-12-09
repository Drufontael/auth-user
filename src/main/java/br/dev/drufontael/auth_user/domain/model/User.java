package br.dev.drufontael.auth_user.domain.model;

import java.time.LocalDateTime;
import java.util.*;

public class User {
    private UUID id;
    private String username;
    private String email;
    private String password;
    private LocalDateTime createdAt = LocalDateTime.now();
    private List<LocalDateTime> logins = new ArrayList<>();
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<LocalDateTime> getLogins() {
        return logins;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public User setId(UUID id) {
        this.id = id;
        return this;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public User setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public User setLogins(List<LocalDateTime> logins) {
        this.logins = logins;
        return this;
    }

    public User setRoles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    private User(Builder builder) {
       this.id = builder.id;
       this.username = builder.username;
       this.email = builder.email;
       this.password = builder.password;
       this.createdAt = builder.createdAt;
       this.logins = builder.logins;
       this.roles = builder.roles;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String username;
        private String email;
        private String password;
        private LocalDateTime createdAt= LocalDateTime.now();
        private List<LocalDateTime> logins= new ArrayList<>();
        private Set<Role> roles= new HashSet<>();

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder logins(List<LocalDateTime> logins) {
            this.logins = logins;
            return this;
        }

        public Builder roles(Set<Role> roles) {
            this.roles = roles;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        return Objects.equals(id, user.id) && email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
