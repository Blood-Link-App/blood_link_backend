package org.springframework.blood_link_server.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur pour les opérations de développement
 * ⚠️ À SUPPRIMER EN PRODUCTION ⚠️
 */
@RestController
@RequestMapping("api/v1/dev")
@RequiredArgsConstructor
public class DevController {

    private final UserRepository userRepository;

    /**
     * Supprimer tous les utilisateurs (pour les tests)
     * DELETE /api/v1/dev/users/all
     */
    @DeleteMapping("/users/all")
    public ResponseEntity<?> deleteAllUsers() {
        try {
            long count = userRepository.count();
            userRepository.deleteAll();
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "All users deleted successfully");
            response.put("deletedCount", count);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * Supprimer un utilisateur par email
     * DELETE /api/v1/dev/users/by-email?email=xxx@xxx.com
     */
    @DeleteMapping("/users/by-email")
    public ResponseEntity<?> deleteUserByEmail(@RequestParam String email) {
        try {
            var user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                userRepository.delete(user.get());
                Map<String, String> response = new HashMap<>();
                response.put("message", "User deleted successfully");
                response.put("email", email);
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "User not found");
                error.put("email", email);
                return ResponseEntity.status(404).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * Compter le nombre d'utilisateurs
     * GET /api/v1/dev/users/count
     */
    @GetMapping("/users/count")
    public ResponseEntity<?> countUsers() {
        Map<String, Long> response = new HashMap<>();
        response.put("count", userRepository.count());
        return ResponseEntity.ok(response);
    }
}
