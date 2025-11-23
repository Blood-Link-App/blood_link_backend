package org.springframework.blood_link_server.services.interfaces;

import org.springframework.blood_link_server.models.metiers.User;

public interface UserService {
    User getMe(String username);
}
