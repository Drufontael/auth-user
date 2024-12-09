package br.dev.drufontael.auth_user.persistence.entities;

import br.dev.drufontael.auth_user.domain.model.Role;
import br.dev.drufontael.auth_user.domain.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity(name = "tb_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(length = 200,nullable = false)
    private String username;
    @Column(unique = true,nullable = false)
    private String email;
    private String password;
    private LocalDateTime createdAt;
    @CollectionTable(name = "tb_user_login",joinColumns = @JoinColumn(name = "user_id"))
    @ElementCollection(fetch = FetchType.EAGER)
    private List<LocalDateTime> logins;
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "tb_user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    public UserEntity(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.createdAt = user.getCreatedAt();
        this.roles = user.getRoles();
        this.logins = user.getLogins();
    }

    public User toDomain() {
        return new User.Builder()
                .id(id)
                .username(username)
                .email(email)
                .password(password)
                .createdAt(createdAt)
                .logins(logins)
                .roles(roles)
                .build();
    }
}
