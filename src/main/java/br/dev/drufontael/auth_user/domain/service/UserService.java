package br.dev.drufontael.auth_user.domain.service;

import br.dev.drufontael.auth_user.domain.exceptions.AccessDeniedException;
import br.dev.drufontael.auth_user.domain.exceptions.InvalidEmailFormatException;
import br.dev.drufontael.auth_user.domain.exceptions.UserAlreadExistsException;
import br.dev.drufontael.auth_user.domain.exceptions.UserNotFoundException;
import br.dev.drufontael.auth_user.domain.model.Access;
import br.dev.drufontael.auth_user.domain.model.Role;
import br.dev.drufontael.auth_user.domain.model.User;
import br.dev.drufontael.auth_user.domain.ports.in.Encoder;
import br.dev.drufontael.auth_user.domain.ports.in.UserManager;
import br.dev.drufontael.auth_user.domain.ports.out.UserPersistence;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserService implements UserManager {

    private static final String REGEX_EMAIL = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})$";
    private final UserPersistence userPersistence;

    public UserService(UserPersistence userPersistence) {
        this.userPersistence = userPersistence;
    }

    @Override
    public User register(User user, Encoder encoder) {
        if(!validateEmail(user.getEmail())) throw new InvalidEmailFormatException("Invalid email format");
        userPersistence.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new UserAlreadExistsException(String.format("the email %s is already registered with another user.", user.getEmail()));
        });
        user.setPassword(encoder.encode(user.getPassword()));
        user.getRoles().add(Role.GUEST);
        return userPersistence.save(user);
    }

    @Override
    public User register(User user, Encoder encoder, Role role) {
        if(!validateEmail(user.getEmail())) throw new InvalidEmailFormatException("Invalid email format");
        userPersistence.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new UserAlreadExistsException(String.format("the email %s is already registered with another user.", user.getEmail()));
        });
        user.setPassword(encoder.encode(user.getPassword()));
        user.getRoles().add(role);
        return userPersistence.save(user);
    }


    @Override
    public Access login(String username, String password, Encoder encoder) {
        User user = userPersistence.findByUsername(username).orElseThrow(()->new UserNotFoundException("User not found:"+username));
        if(!encoder.matches(password, user.getPassword())) throw new AccessDeniedException("Access denied");
        Role role=user.getRoles().stream().min(Role::compareTo).orElse(Role.GUEST);
        Date expirationTime = Date.from(LocalDateTime.now().plusMinutes(role.expiration()).atZone(ZoneId.systemDefault()).toInstant());
        Map<String, Object> claims = Map.of("role", role.role, "email", user.getEmail());
        user.getLogins().add(LocalDateTime.now());
        userPersistence.save(user);
        return Access.builder().subject(user.getUsername()).expiresAt(expirationTime).claims(claims).build();
    }

    @Override
    public Access loginByEmail(String email, String password, Encoder encoder) {
        if(!validateEmail(email)) throw new InvalidEmailFormatException("Invalid email format");
        User user = userPersistence.findByEmail(email).orElseThrow(()->new UserNotFoundException("User not found:"+email));
        if(!encoder.matches(password, user.getPassword())) throw new AccessDeniedException("Access denied");
        Role role=user.getRoles().stream().min(Role::compareTo).orElse(Role.GUEST);
        Date expirationTime = Date.from(LocalDateTime.now().plusMinutes(role.expiration()).atZone(ZoneId.systemDefault()).toInstant());
        Map<String, Object> claims = Map.of("role", role.role,"username", user.getUsername());
        user.getLogins().add(LocalDateTime.now());
        userPersistence.save(user);
        return Access.builder().subject(user.getEmail()).expiresAt(expirationTime).claims(claims).build();
    }

    @Override
    public User info(String username) {
        return userPersistence.findByUsername(username).orElseThrow(()->new UserNotFoundException("User not found:"+username));
    }

    @Override
    public User info(UUID id) {
        return userPersistence.findById(id).orElseThrow(()->new UserNotFoundException("User not found:"+id));
    }

    @Override
    public List<User> listAllUsers() {
        return userPersistence.listAllUsers();
    }

    @Override
    public void addRole(UUID id, Role role) {
        userPersistence.findById(id).ifPresentOrElse(user -> {
            user.getRoles().add(role);
            userPersistence.save(user);
        },()->{throw new UserNotFoundException("User not found:"+id);});
    }

    @Override
    public void removeRole(UUID id, Role role) {
        userPersistence.findById(id).ifPresentOrElse(user -> {
            user.getRoles().remove(role);
            userPersistence.save(user);
        },()->{throw new UserNotFoundException("User not found:"+id);});
    }

    @Override
    public void delete(UUID id) {
        userPersistence.findById(id).ifPresentOrElse(userPersistence::delete,
                ()->{throw new UserNotFoundException("User not found:"+id);});
    }

    @Override
    public User update(UUID id, User user) {
        userPersistence.findById(id).ifPresentOrElse(u ->{
            user.setId(id);
            userPersistence.save(user);
        },()->{throw new UserNotFoundException("User not found:"+id);});
        return user;
    }

    private boolean validateEmail(String email) {
       return email.matches(REGEX_EMAIL);
    }
}
