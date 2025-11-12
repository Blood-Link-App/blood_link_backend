package org.springframework.blood_link_server.services.implementations;

import org.springframework.blood_link_server.models.dtos.requests.LoginRequest;
import org.springframework.blood_link_server.models.dtos.requests.RegisterRequest;
import org.springframework.blood_link_server.models.dtos.responses.AuthenticationResponse;
import org.springframework.blood_link_server.models.enumerations.UserRole;
import org.springframework.blood_link_server.models.metiers.BloodBank;
import org.springframework.blood_link_server.models.metiers.Doctor;
import org.springframework.blood_link_server.models.metiers.Donor;
import org.springframework.blood_link_server.models.metiers.User;
import org.springframework.blood_link_server.repositories.BloodBankRepository;
import org.springframework.blood_link_server.repositories.UserRepository;
import org.springframework.blood_link_server.services.interfaces.AuthenticationService;
import org.springframework.blood_link_server.services.interfaces.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BloodBankRepository bloodBankRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, BloodBankRepository bloodBankRepository, JwtService jwtService, AuthenticationManager authManager){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.bloodBankRepository = bloodBankRepository;
        this.jwtService = jwtService;
        this.authManager = authManager;
    }

    @Override
    public AuthenticationResponse registerUser(RegisterRequest request) {

        if(checkAttribute(request)){

        validateUniqueConstraints(request);


        User user = createUserByRole(request);

        setCommonProperties(user, request);

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user.getEmail());


        return AuthenticationResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .role(user.getUserRole().name())
                .build();
        }
        throw new IllegalArgumentException("Missing required fields for role : " + request.getUserRole());

    }

    @Override
    public AuthenticationResponse login(LoginRequest request){
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String jwtToken = jwtService.generateToken(user.getEmail());

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .role(user.getUserRole().name())
                .build();
    }


    private boolean checkAttribute(RegisterRequest request)
    {
        return request.getEmail() != null &&
                request.getPassword() != null &&
                request.getPhoneNumber() != null &&
                request.getUserRole() != null && (
                        (
                         request.getUserRole() == UserRole.BLOODBANK &&
                                 request.getBloodBankName() != null &&
                                 request.getAddress() != null
                ) || (
                        request.getUserRole() == UserRole.DOCTOR &&
                                request.getName() != null &&
                                request.getSurname() != null &&
                                request.getSpeciality() != null &&
                                request.getHospitalName() != null
                ) || (
                        request.getUserRole() == UserRole.DONOR &&
                                request.getName() != null &&
                                request.getSurname() != null &&
                                request.getBloodType() != null &&
                                request.getLastDonationDate() != null
                )
        );
    }

    private void validateUniqueConstraints(RegisterRequest request){
        if (userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new IllegalArgumentException("This email is already used");
        }
        if(userRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()){
            throw new IllegalArgumentException(("This phone number is already in use"));
        }
        if(request.getUserRole() == UserRole.BLOODBANK && bloodBankRepository.findByBloodBankName(request.getBloodBankName()).isPresent()){
            throw new IllegalArgumentException("There's already a bloodbank with the same name");
        }
    }

    private User createUserByRole(RegisterRequest request){

        return switch (request.getUserRole()){

            case BLOODBANK -> {
                BloodBank bloodBank = new BloodBank();
                bloodBank.setAddress(request.getAddress());
                bloodBank.setBloodBankName(request.getBloodBankName());
                bloodBank.setLicenseNumber(
                        request.getLicenseNumber() != null
                        ? request.getLicenseNumber()
                                : UUID.randomUUID()
                );
                bloodBank.setUserRole(UserRole.BLOODBANK);

                yield bloodBank;
            }
            case DOCTOR -> {
                Doctor doctor = new Doctor();
                doctor.setName(request.getName());
                doctor.setSurname(request.getSurname());
                doctor.setLicenseNumber(
                        request.getOrderLicense()!= null
                        ? request.getOrderLicense()
                                : UUID.randomUUID()
                );
                doctor.setSpeciality(request.getSpeciality());
                doctor.setHospitalName(request.getHospitalName());
                doctor.setUserRole(UserRole.DOCTOR);

                yield doctor;
            }

            case DONOR -> {
                Donor donor = new Donor();
                donor.setBloodType(request.getBloodType());
                donor.setName(request.getName());
                donor.setSurname(request.getSurname());
                donor.setLastDonationDate(request.getLastDonationDate());
                donor.setUserRole(UserRole.DONOR);
                yield donor;
            }
        };
    }

    private void setCommonProperties(User user, RegisterRequest request){
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
    }
}
