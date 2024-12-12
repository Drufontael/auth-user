package br.dev.drufontael.auth_user.configuration.security.jwt;

import br.dev.drufontael.auth_user.domain.model.Access;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    public AcessToken generateToken(Access access) {

        var key =SecretKeyGenerator.getKey();

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
        var key = SecretKeyGenerator.getKey();
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
            throw new InvalidTokenException("Invalid token");
        }
    }
}
