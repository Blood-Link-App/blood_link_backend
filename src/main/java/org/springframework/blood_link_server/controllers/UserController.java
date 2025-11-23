package org.springframework.blood_link_server.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.models.metiers.User;
import org.springframework.blood_link_server.services.interfaces.JwtService;
import org.springframework.blood_link_server.services.interfaces.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("get-me")
    public ResponseEntity<?> getMe(HttpServletRequest httpServlet) {
        String username = getUsername(httpServlet);
        User user = userService.getMe(username);
        return ResponseEntity.ok(user);
    }

    private String getUsername(HttpServletRequest http) {
        final String authHeader = http.getHeader("Authorization");
        var jwt = authHeader.substring(7);
        return jwtService.extractUsername(jwt);
    }
}
