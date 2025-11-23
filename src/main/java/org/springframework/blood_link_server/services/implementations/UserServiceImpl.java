package org.springframework.blood_link_server.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.models.metiers.User;
import org.springframework.blood_link_server.repositories.UserRepository;
import org.springframework.blood_link_server.services.interfaces.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * @param username of the user
     * @return the user
     */
    @Override
    public User getMe(String username) {
        return userRepository.findByEmail(username).orElse(null);
    }
}
