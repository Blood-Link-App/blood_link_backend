package org.springframework.blood_link_server.models.metiers;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.blood_link_server.models.appl.BloodRequest;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

@Entity
@Table(name = "doctor")

public class Doctor extends Person{

    @Column(name = "license_number", nullable = false)
    private UUID licenseNumber;

    @Column(name = "hospital_name", nullable = false)
    private String hospitalName;

    @Column(name = "speciality", nullable = false)
    private String speciality;

//    @JsonIgnore
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BloodRequest> bloodRequests= new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "doctor_bloodbank_affiliation", joinColumns = @JoinColumn(name = "doctor_id"), inverseJoinColumns = @JoinColumn(name = "bloodbank_id")
    )
    private Set<BloodBank> affiliatedDoctorBanks = new HashSet<>();
}
