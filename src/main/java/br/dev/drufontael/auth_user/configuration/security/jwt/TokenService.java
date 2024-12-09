package br.dev.drufontael.auth_user.configuration.security.jwt;

import br.dev.drufontael.auth_user.domain.model.Access;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final SecretKeyGenerator secretKeyGenerator;

    public AcessToken generateToken(Access access) {

        var key =secretKeyGenerator.getKey();

        String token = Jwts
                .builder()
                .signWith(key)
                .setSubject(access.subject())
                .setExpiration(access.expiresAt())
                .addClaims(access.claims())
                .compact();

        return new AcessToken(token);
    }

    public Access getAccess(AcessToken token) {
        var key = secretKeyGenerator.getKey();
        try {
            Claims build = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token.getAcessToken())
                    .getBody();
            return Access.builder()
                    .subject(build.getSubject())
                    .expiresAt(build.getExpiration())
                    .claims(build)
                    .build();


        }catch (JwtException e){
            throw new RuntimeException("Token inválido");
        }
    }
}
