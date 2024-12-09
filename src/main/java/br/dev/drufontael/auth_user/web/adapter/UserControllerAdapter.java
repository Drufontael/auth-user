package br.dev.drufontael.auth_user.web.adapter;

import br.dev.drufontael.auth_user.configuration.security.jwt.AcessToken;
import br.dev.drufontael.auth_user.configuration.security.jwt.TokenService;
import br.dev.drufontael.auth_user.domain.exceptions.*;
import br.dev.drufontael.auth_user.domain.model.Access;
import br.dev.drufontael.auth_user.domain.model.Role;
import br.dev.drufontael.auth_user.domain.model.User;
import br.dev.drufontael.auth_user.domain.ports.in.Encoder;
import br.dev.drufontael.auth_user.domain.ports.in.UserManager;
import br.dev.drufontael.auth_user.web.dto.UserDetailsDto;
import br.dev.drufontael.auth_user.web.dto.UserDto;
import br.dev.drufontael.auth_user.web.dto.UserItemListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserControllerAdapter {

    private final UserManager manager;
    private final Encoder encoder;
    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody UserDto register) {
        User user = manager.register(register.toDomain(), encoder);
        log.info("User {} created with id {} at {}", user.getUsername(), user.getId(), user.getCreatedAt());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto login) {
        Access access = (login.email() != null)
                ? manager.loginByEmail(login.email(), login.password(), encoder)
                : manager.login(login.username(), login.password(), encoder);
        AcessToken token = tokenService.generateToken(access);
        log.info("Subject {} logged in", access.subject());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/manager")
    public ResponseEntity<?> registerWithRole(@RequestBody UserDto register) {
        Role role;
        try {
            role = Role.valueOf(register.role().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Invalid role");
        }
        User user = manager.register(register.toDomain(), encoder, role);
        log.info("User {} created with id {} at {}", user.getUsername(), user.getId(), user.getCreatedAt());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/manager")
    public ResponseEntity<List<UserItemListDto>> listAllUsers() {
        List<UserItemListDto> users = manager.listAllUsers()
                .stream()
                .map(UserItemListDto::fromUser)
                .toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/manager/info")
    public ResponseEntity<?> info(@RequestParam(required = false) String username, @RequestParam(required = false) UUID id) {
        if (id == null && username == null) {
            throw new InvalidRequestException("Request without valid parameter");
        }
        User user = (username != null) ? manager.info(username) : manager.info(id);
        log.info("id {} user data sent", user.getId());
        return ResponseEntity.ok(UserDetailsDto.fromUser(user));
    }

    @PostMapping("/manager/{id}/role")
    public ResponseEntity<?> addRole(@PathVariable UUID id, @RequestBody RoleDto role) {
        try {
            manager.addRole(id, Role.valueOf(role.role()));
            log.info("id {} role {} added", id, role.role());
            return ResponseEntity.ok().build();
        }catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Invalid role name");
        }
    }

    @DeleteMapping("/manager/{id}/role")
    public ResponseEntity<?> removeRole(@PathVariable UUID id, @RequestBody RoleDto role) {
        try {
            manager.removeRole(id, Role.valueOf(role.role()));
            log.info("id {} role {} removed", id, role.role());
            return ResponseEntity.ok().build();
        }catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Invalid role name");
        }
    }

    @PutMapping("/manager/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody UserDto user) {
        manager.update(id, user.toDomain());
        log.info("id {} updated", id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/manager/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
            manager.delete(id);
            log.info("id {} deleted", id);
            return ResponseEntity.ok().build();
    }

    private record RoleDto(String role) {}

}

