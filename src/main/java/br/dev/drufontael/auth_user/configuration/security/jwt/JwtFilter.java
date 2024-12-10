package br.dev.drufontael.auth_user.configuration.security.jwt;

import br.dev.drufontael.auth_user.domain.model.Access;
import br.dev.drufontael.auth_user.domain.model.User;
import br.dev.drufontael.auth_user.domain.model.Role;
import br.dev.drufontael.auth_user.domain.ports.in.UserManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserManager manager;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = getToken(request);
        if (token != null) {
            try {
                Access access = tokenService.getAccess(new AcessToken(token));
                String username;
                if (access.claims().get("username") != null) {
                    username = access.claims().get("username").toString();
                } else{
                    username = access.subject();
                }
                User user = manager.info(username);
                setUserAsAuthenticated(user);
            }catch (InvalidTokenException e){
                log.error("Invalid token: ", e);
            }catch (Exception e){
                log.error("Unexpected error in token validation: ", e);
            }
        }
        filterChain.doFilter(request, response);
    }

    private void setUserAsAuthenticated(User user) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRoles().stream().map(Role::getRole).toArray(String[]::new))
                .build();
        UsernamePasswordAuthenticationToken authentication=
                new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
