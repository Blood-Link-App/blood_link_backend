package org.springframework.blood_link_server.models.metiers;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.blood_link_server.models.appl.BloodBankStock;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

@Entity
@Table(name="blood_bank")

public class BloodBank extends User{

    @Column(name = "blood_bank_name", nullable = false, unique = true)
    private String bloodBankName;

    @Column(name= "license_number", nullable = false)
    private UUID licenseNumber;

    @Column(name= "address", nullable = false)
    private String address;

    @OneToOne(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "blood_bank_stock_id")
    private BloodBankStock stock;
//
//    @ManyToMany(mappedBy = "affiliatedDoctorBanks")
//    private Set<Doctor> affiliatedDoctors = new HashSet<>();

    @ManyToMany(mappedBy = "affiliatedDonorBanks")
    private Set <Donor> affiliatedDonors = new HashSet<>();
}
