package org.springframework.blood_link_server.models.metiers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.blood_link_server.models.appl.DonationRequest;
import org.springframework.blood_link_server.models.appl.Notification;
import org.springframework.blood_link_server.models.enumerations.BloodType;
import org.springframework.blood_link_server.models.medicalProfile.MedicalProfile;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

@Entity
@Table(name = "donor")
public class Donor extends Person{

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_type", nullable = false)
    private BloodType bloodType;

    @Column(name = "last_donation_date")
    private LocalDate lastDonationDate;

    @JsonIgnore
    @OneToMany(mappedBy = "donor", cascade = CascadeType.ALL)
    private List<Notification> notifications ;

    @JsonIgnore
    @OneToMany(mappedBy = "donor", cascade = CascadeType.ALL)
    private List<DonationRequest> donationRequests;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "medical_profile_id")
    private MedicalProfile medicalProfile;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(
             name = "donor_blood_affiliation", joinColumns = @JoinColumn(name = "donor_id"), inverseJoinColumns = @JoinColumn(name ="bloodbank_id"))
    private Set<BloodBank> affiliatedDonorBanks = new HashSet<>();

    public void affiliateNewBank(BloodBank bloodBank) {
        this.affiliatedDonorBanks.add(bloodBank);
    }

    public void removeAffiliateBank(BloodBank bloodBank) {
        this.affiliatedDonorBanks.remove(bloodBank);
    }

    public boolean isAffiliated(BloodBank bank){
        return affiliatedDonorBanks.contains(bank);
    }
}
