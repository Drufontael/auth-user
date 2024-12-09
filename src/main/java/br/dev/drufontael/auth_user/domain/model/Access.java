package br.dev.drufontael.auth_user.domain.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Access {
    private String subject;
    private Date expiresAt;
    private Map<String, Object> claims;

    private Access(Builder builder) {
        this.subject = builder.subject;
        this.expiresAt = builder.expiresAt;
        this.claims = builder.claims;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder{
        private String subject;
        private Date expiresAt;
        private Map<String, Object> claims=new HashMap<>();

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder expiresAt(Date expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public Builder claims(Map<String, Object> claims) {
            this.claims = claims;
            return this;
        }

        public Access build() {
            return new Access(this);
        }
    }

    public String subject() {
        return subject;
    }

    public Date expiresAt() {
        return expiresAt;
    }

    public Map<String, Object> claims() {
        return claims;
    }

    @Override
    public String toString() {
        return "Access{" +
                "subject='" + subject + '\'' +
                ", expiresAt=" + expiresAt +
                ", claims=" + claims +
                '}';
    }
}
