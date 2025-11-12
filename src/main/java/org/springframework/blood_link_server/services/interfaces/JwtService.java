package org.springframework.blood_link_server.services.interfaces;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;

public interface JwtService {

    String generateToken(String username);

    String extractUsername(String token);

    String generateToken(Map<String, Object> extraClaims, String username);

    boolean isTokenValid(String token, UserDetails userDetails);

    boolean isTokenExpired(String token);

    Date extractExpiration(String token);
}
