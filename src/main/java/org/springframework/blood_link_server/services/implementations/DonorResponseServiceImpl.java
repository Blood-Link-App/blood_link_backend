package org.springframework.blood_link_server.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.models.appl.DonorResponse;
import org.springframework.blood_link_server.models.appl.Notification;
import org.springframework.blood_link_server.models.dtos.requests.ResponseRequest;
import org.springframework.blood_link_server.models.metiers.Donor;
import org.springframework.blood_link_server.repositories.DonorRepository;
import org.springframework.blood_link_server.repositories.NotificationRepository;
import org.springframework.blood_link_server.services.interfaces.DonorResponseService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor

public class DonorResponseServiceImpl implements DonorResponseService {

    private final NotificationRepository notificationRepository;
    private final DonorRepository donorRepository;

    /**
     * @param notifID  id of the notif to respond to
     * @param username of the donor who wants to respond
     * @param responseDTo the structure of the response
     * @return donorResponse
     */

    @Override
    public DonorResponse createResponseToAnAlertNotification(UUID notifID, String username, ResponseRequest responseDTo) {

        Notification notif = notificationRepository.findById(notifID).orElse(null);

        Donor donor =donorRepository.findByEmail(username).orElse(null);

        if (notif == null){
            throw new IllegalArgumentException("Notification not found");
        }

        if(donor == null){
            throw new IllegalArgumentException("Notification not found");
        }

        if(!notif.getDonor().getId().equals(donor.getId())){
            throw new AccessDeniedException("Unauthorized user !!");
        }

        DonorResponse response = new DonorResponse();
        response.setResponse(responseDTo.response());

        notif.setDonorResponse(response);

        return notificationRepository.save(notif).getDonorResponse();
    }
}
