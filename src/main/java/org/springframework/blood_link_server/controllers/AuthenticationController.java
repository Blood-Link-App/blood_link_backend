package org.springframework.blood_link_server.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.blood_link_server.models.dtos.requests.LoginRequest;
import org.springframework.blood_link_server.models.dtos.requests.RegisterRequest;
import org.springframework.blood_link_server.models.dtos.responses.AuthenticationResponse;
import org.springframework.blood_link_server.models.dtos.responses.ErrorResponse;
import org.springframework.blood_link_server.models.metiers.User;
import org.springframework.blood_link_server.services.interfaces.AuthenticationService;
import org.springframework.blood_link_server.services.interfaces.JwtService;
import org.springframework.blood_link_server.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/v1/auth/")

public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final JwtService jwtService;

    public AuthenticationController(AuthenticationService authenticationService, UserService userService, JwtService jwtService){
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
    * Register a new user
     * POST /api/v1/auth/signUp
    * */

    @PostMapping("/signUp")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request){
        try {

            AuthenticationResponse response = authenticationService.registerUser(request);
            return ResponseEntity.ok(response);
        }catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e);
        }
    }

    @PostMapping("/logIn")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        try{
            AuthenticationResponse response = authenticationService.login(request);
            return  ResponseEntity.ok(response);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("User not found"));
        }
    }

    /**
     * Get current authenticated user
     * GET /api/v1/auth/current-user
     */
    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest httpServlet) {
        try {
            String username = extractUsername(httpServlet);
            User user = userService.getMe(username);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Unauthorized"));
        }
    }

    private String extractUsername(HttpServletRequest http) {
        final String authHeader = http.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid authorization header");
        }
        var jwt = authHeader.substring(7);
        return jwtService.extractUsername(jwt);
    }
}
