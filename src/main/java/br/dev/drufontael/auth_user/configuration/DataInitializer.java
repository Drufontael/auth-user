package br.dev.drufontael.auth_user.configuration;

import br.dev.drufontael.auth_user.domain.exceptions.UserNotFoundException;
import br.dev.drufontael.auth_user.domain.model.Role;
import br.dev.drufontael.auth_user.domain.model.User;
import br.dev.drufontael.auth_user.domain.ports.in.Encoder;
import br.dev.drufontael.auth_user.domain.ports.in.UserManager;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataInitializer {

    @Value("${security.config.root.username}")
    private String rootUsername;
    @Value("${security.config.root.password}")
    private String rootPassword;
    @Value("${security.config.root.email}")
    private String rootEmail;

    private final UserManager manager;
    private final Encoder encoder;

    @PostConstruct
    private void init() {
        initRootUser();
        initServices();
    }

    private void initRootUser() {
        try {
            manager.info(rootUsername);
            log.info("Root user already exists");
        } catch (UserNotFoundException e) {
            User root = User.builder().
                    username(rootUsername).
                    email(rootEmail).
                    password(rootPassword).build();
            manager.register(root, encoder, Role.ADMIN);
            log.info("Root user created");
        }
    }

    private void initServices() {
        try {
            manager.info("concierge");
            log.info("Concierge user already exists");
        } catch (UserNotFoundException e) {
            User concierge = User.builder().
                    username("concierge").
                    email("SxV3T@example.com").
                    password("123456").build();
            manager.register(concierge, encoder, Role.SERVICE);
            log.info("Concierge user created");
        }
    }
}
