package org.springframework.blood_link_server.models.enumerations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public enum UserRole{
    BLOODBANK("bloodBank"),
    BLOOD_BANK("bloodBank"),  // Alias pour compatibilité mobile
    DOCTOR("doctor"),
    DONOR("donor");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static UserRole fromString(String value) {
        if (value == null) {
            return null;
        }
        for (UserRole role : UserRole.values()) {
            if (role.value.equalsIgnoreCase(value) || role.name().equalsIgnoreCase(value)) {
                return role == BLOOD_BANK ? BLOODBANK : role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + value);
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + (this == BLOOD_BANK ? "BLOODBANK" : this.name())));
    }
}
