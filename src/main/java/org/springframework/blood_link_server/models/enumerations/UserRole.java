package org.springframework.blood_link_server.models.enumerations;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public enum UserRole{
    BLOODBANK,
    DOCTOR,
    DONOR;


    public List<SimpleGrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.name()));
    }
}
