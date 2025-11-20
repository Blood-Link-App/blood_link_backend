package org.springframework.blood_link_server.models.dtos.requests;

import org.springframework.blood_link_server.models.enumerations.Gender;

import java.time.LocalDate;

public record UpdateProfileRequest(

        LocalDate birthdate,

        Gender gender,

        float weight,

        String emergencyContact,

        float hemoglobinLevel,

        float bodyTemperature,

        float pulseRate,

        //General health questions
        Boolean hasTattoosWithinLast6Months,

        Boolean hasSurgeryWithinLast6_12Months,

        Boolean hasChronicalIllness,

        Boolean hasTravelledWithinLast3Months,

        Boolean hasPiercingWithinLast7Months,

        Boolean isOnMedication,


        // specific questions to female
        Boolean isPregnant,

        Boolean isBreastFeeding,

        Boolean isChildBirthWithinLast3Months,

        Boolean hasHeavyMenstrualFlow,

        LocalDate lastMenstrualPeriod
) {}
