package org.springframework.blood_link_server.models.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


import org.springframework.blood_link_server.models.enumerations.BloodType;
import org.springframework.blood_link_server.models.enumerations.UserRole;

import java.util.UUID;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class RegisterRequest {

    // User information
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phoneNumber;
    private UserRole userRole;

    // Donor's information
    @JsonProperty("LastDonationDate")
    private String lastDonationDate;
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
