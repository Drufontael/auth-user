package br.dev.drufontael.auth_user.domain.ports.in;

import br.dev.drufontael.auth_user.domain.model.Access;
import br.dev.drufontael.auth_user.domain.model.Role;
import br.dev.drufontael.auth_user.domain.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

public interface UserManager {

    User register(User user, Encoder encoder);
    User register(User user, Encoder encoder, Role role);
    Access login(String username, String password, Encoder encoder);
    Access loginByEmail(String email, String password,Encoder encoder);
    User info(String username);
    User info(UUID id);
    List<User> listAllUsers();
    void addRole(UUID id, Role role);
    void removeRole(UUID id, Role role);
    void delete(UUID id);
    User update(UUID id, User user);
}
