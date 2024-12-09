package br.dev.drufontael.auth_user.persistence.adapters;

import br.dev.drufontael.auth_user.domain.exceptions.UserNotFoundException;
import br.dev.drufontael.auth_user.domain.model.User;
import br.dev.drufontael.auth_user.domain.ports.out.UserPersistence;
import br.dev.drufontael.auth_user.persistence.entities.UserEntity;
import br.dev.drufontael.auth_user.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPersistence {

    private final UserRepository repository;


    @Override
    @Transactional
    public User save(User user) {
        return repository.save(new UserEntity(user)).toDomain();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username).map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email).map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findById(id).map(UserEntity::toDomain);
    }

    @Override
    public List<User> listAllUsers() {
        return repository.findAll().stream().map(UserEntity::toDomain).toList();
    }

    @Override
    public void delete(User user) {
        repository.delete(new UserEntity(user));
    }


}
