package org.springframework.blood_link_server.models.enumerations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum BloodType {
    A_POSITIVE("A+"),
    A_NEGATIVE("A-"),
    AB_POSITIVE("AB+"),
    AB_NEGATIVE("AB-"),
    B_POSITIVE("B+"),
    B_NEGATIVE("B-"),
    O_POSITIVE("O+"),
    O_NEGATIVE("O-");

    private final String bloodType;

    public static BloodType fromString(String bloodType){
        for (BloodType type : values())
            if (type.name().equalsIgnoreCase(bloodType))
                return type;
        throw new IllegalArgumentException("Invalid Blood type: " + bloodType);
    }


    /**
     * Returns a list of compatible blood types to a specific type
     *
     * @Param recipient blood type
     * @return list of compatible blood types
     */

    public static List<BloodType> getCompatibleDonors (BloodType recipientType){
        return switch (recipientType) {
            case A_POSITIVE  -> List.of(A_POSITIVE, A_NEGATIVE, O_POSITIVE, O_NEGATIVE);
            case A_NEGATIVE  -> List.of(A_NEGATIVE, O_NEGATIVE);
            case B_POSITIVE  -> List.of(O_NEGATIVE, O_POSITIVE, B_NEGATIVE, B_POSITIVE);
            case B_NEGATIVE  -> List.of(O_NEGATIVE, B_NEGATIVE);
            case AB_POSITIVE -> List.of(O_POSITIVE, A_POSITIVE, B_POSITIVE, AB_POSITIVE, O_NEGATIVE, A_NEGATIVE, AB_NEGATIVE, B_NEGATIVE);
            case AB_NEGATIVE -> List.of(O_NEGATIVE, A_NEGATIVE, B_NEGATIVE, AB_NEGATIVE);
            case O_POSITIVE  -> List.of(O_NEGATIVE, O_POSITIVE);
            case O_NEGATIVE  -> List.of(O_NEGATIVE);
        };
    }

    /**
     * Given the donor blood type it returns a list of recipient blood types
     *
     * @Param donor blood type
     * @return  list of compatible blood type
     */

    public static List<BloodType> getCompatiblesRecipients (BloodType donorType){
        return switch (donorType) {
            case A_POSITIVE -> List.of(A_POSITIVE, AB_POSITIVE);
            case A_NEGATIVE -> List.of(A_NEGATIVE, A_POSITIVE, AB_POSITIVE, AB_NEGATIVE);
            case AB_POSITIVE -> List.of(AB_POSITIVE);
            case AB_NEGATIVE -> List.of(AB_POSITIVE, AB_NEGATIVE);
            case B_POSITIVE -> List.of(B_POSITIVE, AB_POSITIVE);
            case B_NEGATIVE -> List.of(B_POSITIVE, B_NEGATIVE, AB_POSITIVE, AB_NEGATIVE);
            case O_POSITIVE -> List.of(A_POSITIVE, B_POSITIVE, AB_POSITIVE, O_NEGATIVE);
            case O_NEGATIVE -> List.of(A_POSITIVE, A_NEGATIVE, B_POSITIVE, B_NEGATIVE, AB_POSITIVE, AB_NEGATIVE, O_POSITIVE, O_NEGATIVE);
        };
    }

    /**
     * Checks if a donor can donate to a recipient according to his blood type
     *
     * @Param donor blood type
     * @Param recipient blood type
     * @return true if blood types are compatible
     */

    public static boolean isCompatibleDonor(BloodType donorType, BloodType recipientType){
        return getCompatibleDonors(recipientType).contains(donorType);
    }

    /**
     * Checks if a recipient can receive from a donor according to his blood type
     *
     * @Param donor blood type
     * @Param recipient blood type
     * @return true if blood types are compatible
     */

    public static boolean isCompatibleRecipient(BloodType donorType, BloodType recipientType){
        return getCompatiblesRecipients(donorType).contains(recipientType);
    }
}
