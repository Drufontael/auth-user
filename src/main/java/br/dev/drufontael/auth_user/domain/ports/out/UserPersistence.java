package br.dev.drufontael.auth_user.domain.ports.out;

import br.dev.drufontael.auth_user.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserPersistence {

    User save(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);
    List<User> listAllUsers();
    void delete(User user);
}
