package org.springframework.blood_link_server.models.medicalProfile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

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

   public HealthFemaleQuestions(boolean hasTattoosWithinLast6Months , boolean hasSurgeryWithinLast6_12Months, boolean hasChronicalIllness,
                                boolean hasTravelledWithinLast3Months, boolean hasPiercingWithinLast7Months , boolean isOnMedication,
                                boolean isPregnant, boolean isBreastFeeding, boolean hasHeavyMenstrualFlow, boolean isChildBirthWithinLast3Months, LocalDate lastMenstrualPeriod){

       super(hasTattoosWithinLast6Months, hasSurgeryWithinLast6_12Months, hasChronicalIllness,hasTravelledWithinLast3Months, hasPiercingWithinLast7Months, isOnMedication);
       this.isPregnant = isPregnant;
       this.isBreastFeeding = isBreastFeeding;
       this.hasHeavyMenstrualFlow = hasHeavyMenstrualFlow;
       this.isChildBirthWithinLast3Months = isChildBirthWithinLast3Months;
       this.lastMenstrualPeriod = lastMenstrualPeriod;
   }



    @Override
    public boolean hasDeferralIssues(){
        boolean checkWeeks = ChronoUnit.WEEKS.between(lastMenstrualPeriod, LocalDate.now()) >= 2 ;
        return super.hasDeferralIssues() || isPregnant || isBreastFeeding || hasHeavyMenstrualFlow || isChildBirthWithinLast3Months || checkWeeks;
    }
}
