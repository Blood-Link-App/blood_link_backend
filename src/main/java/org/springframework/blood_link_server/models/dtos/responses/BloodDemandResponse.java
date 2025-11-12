package org.springframework.blood_link_server.models.dtos.responses;

import java.util.List;

public record BloodDemandResponse (List<String> bloodBankNames, Long quantity, String doctorName ){
}
