package br.dev.drufontael.auth_user.configuration;

import br.dev.drufontael.auth_user.domain.ports.in.UserManager;
import br.dev.drufontael.auth_user.domain.ports.out.UserPersistence;
import br.dev.drufontael.auth_user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class BeanInitializer {

    private final UserPersistence userPersistence;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserManager userManager() {
        return new UserService(userPersistence);
    }
}
