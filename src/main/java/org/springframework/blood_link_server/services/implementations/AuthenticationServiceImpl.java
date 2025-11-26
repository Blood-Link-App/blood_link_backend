package org.springframework.blood_link_server.services.implementations;

import org.springframework.blood_link_server.models.appl.BloodBankStock;
import org.springframework.blood_link_server.models.appl.StockByType;
import org.springframework.blood_link_server.models.dtos.requests.LoginRequest;
import org.springframework.blood_link_server.models.dtos.requests.RegisterRequest;
import org.springframework.blood_link_server.models.dtos.responses.AuthenticationResponse;
import org.springframework.blood_link_server.models.enumerations.BloodType;
import org.springframework.blood_link_server.models.enumerations.UserRole;
import org.springframework.blood_link_server.models.metiers.BloodBank;
import org.springframework.blood_link_server.models.metiers.Doctor;
import org.springframework.blood_link_server.models.metiers.Donor;
import org.springframework.blood_link_server.models.metiers.User;
import org.springframework.blood_link_server.repositories.BankStockRepository;
import org.springframework.blood_link_server.repositories.BloodBankRepository;
import org.springframework.blood_link_server.repositories.StockByTypeRepository;
import org.springframework.blood_link_server.repositories.UserRepository;
import org.springframework.blood_link_server.services.interfaces.AuthenticationService;
import org.springframework.blood_link_server.services.interfaces.BloodBankService;
import org.springframework.blood_link_server.services.interfaces.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BloodBankRepository bloodBankRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final BloodBankService  bloodBankService;
    private final BankStockRepository  bankStockRepository;
    private final StockByTypeRepository typeRepository;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, BloodBankRepository bloodBankRepository, JwtService jwtService, AuthenticationManager authManager, BloodBankService bloodBankService, BankStockRepository bankStockRepository, StockByTypeRepository typeRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.bloodBankRepository = bloodBankRepository;
        this.jwtService = jwtService;
        this.authManager = authManager;
        this.bloodBankService = bloodBankService;
        this.bankStockRepository = bankStockRepository;
        this.typeRepository = typeRepository;
    }

    @Override
    public AuthenticationResponse registerUser(RegisterRequest request) {

        if(checkAttribute(request)){

        validateUniqueConstraints(request);

        User user = createUserByRole(request);

        setCommonProperties(user, request);

       var user2=  userRepository.saveAndFlush(user);

       if(user2.getUserRole() == UserRole.BLOODBANK || user2.getUserRole() == UserRole.BLOOD_BANK){
           bloodBankService.initializeBloodBankStocks();

           BloodBankStock bankStock = bloodBankService.getBloodBankStockByUsername(user2.getUsername());

           Set<StockByType> types = Arrays.stream(BloodType.values()).map(
                   type -> StockByType.builder()
                           .bloodType(type)
                           .quantity(0L).build()
           ).collect(Collectors.toSet());

          Set<StockByType> stockByTypes = new HashSet<>(typeRepository.saveAllAndFlush(types));
           bankStock.setStockByTypeList(stockByTypes);

           bankStockRepository.save(bankStock);
       }

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
                         (request.getUserRole() == UserRole.BLOODBANK || request.getUserRole() == UserRole.BLOOD_BANK) &&
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
        if((request.getUserRole() == UserRole.BLOODBANK || request.getUserRole() == UserRole.BLOOD_BANK) && bloodBankRepository.findByBloodBankName(request.getBloodBankName()).isPresent()){
            throw new IllegalArgumentException("There's already a bloodbank with the same name");
        }
    }

    private User createUserByRole(RegisterRequest request){

        return switch (request.getUserRole()){

            case BLOODBANK, BLOOD_BANK -> {
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
                // Parse date string to LocalDate
                if (request.getLastDonationDate() != null && !request.getLastDonationDate().isEmpty()) {
                    donor.setLastDonationDate(java.time.LocalDate.parse(request.getLastDonationDate()));
                }
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
