package br.dev.drufontael.auth_user.configuration.security.jwt;


import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class SecretKeyGenerator {

    private SecretKey key;

    public SecretKey getKey() {
        if(key == null) {
            key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
        }
        return key;
    }
}