package org.springframework.blood_link_server.models.medicalProfile;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "health_female_questions")
public class HealthFemaleQuestions extends HealthQuestions{

    @Column(name = "is_pregnant", nullable = false)
    private boolean isPregnant;

    @Column(name = "is_breast_feeding", nullable = false)
    private boolean isBreastFeeding;

    @Column(name = "has_heavy_menstrual_flow", nullable = false)
    private boolean hasHeavyMenstrualFlow;

/*    @Column(name ="has_recent_child_birth")
    private boolean hasRecentChildBirth;*/

    @Column(name = "is_within_3last_months")
    private boolean isChildBirthWithinLast3Months;

    @Column(name = "last_menstrual_period")
    private LocalDate lastMenstrualPeriod;

    //TODO: Write a method which returns the number of days between the last menstrual period date and the date of the day; And complete the function validateAnswers()

    public boolean validateAnswers(){
        return isPregnant || isBreastFeeding || isChildBirthWithinLast3Months || hasHeavyMenstrualFlow;
    }
}
