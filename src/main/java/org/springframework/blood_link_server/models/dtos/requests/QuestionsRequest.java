package org.springframework.blood_link_server.models.dtos.requests;

import java.time.LocalDate;

public record QuestionsRequest(

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
)
{}
