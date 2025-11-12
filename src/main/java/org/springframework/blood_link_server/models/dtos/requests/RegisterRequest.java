package org.springframework.blood_link_server.models.dtos.requests;

import lombok.Builder;
import lombok.Getter;


import org.springframework.blood_link_server.models.enumerations.BloodType;
import org.springframework.blood_link_server.models.enumerations.UserRole;

import java.time.LocalDate;
import java.util.UUID;


@Getter
@Builder

public class RegisterRequest {

    // User information
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phoneNumber;
    private UserRole userRole;

    // Donor's information
    private LocalDate LastDonationDate;
    private BloodType bloodType;

    //Doctor's information
    private UUID orderLicense;
    private String speciality;
    private String hospitalName;

    //BloodBank's information
    private String bloodBankName;
    private String address;
    private UUID licenseNumber;

}
