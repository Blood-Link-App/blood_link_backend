package org.springframework.blood_link_server.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.models.appl.BloodRequest;
import org.springframework.blood_link_server.models.dtos.requests.BloodDemandRequest;
import org.springframework.blood_link_server.models.dtos.responses.ErrorResponse;
import org.springframework.blood_link_server.models.enumerations.RequestStatus;
import org.springframework.blood_link_server.services.interfaces.BloodRequestService;
import org.springframework.blood_link_server.services.interfaces.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("api/blood-request")
@RequiredArgsConstructor
public class BloodRequestController {

    private final JwtService jwtService;
    private final BloodRequestService requestService;

    @PostMapping("create")
    public ResponseEntity<?> createRequest(HttpServletRequest httpRequest, @RequestBody BloodDemandRequest request) {
        //String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        try{

            final String authHeader = httpRequest.getHeader("Authorization");
            var jwt = authHeader.substring(7);
            var username = jwtService.extractUsername(jwt);
            List<BloodRequest> bloodRequests = requestService.sendRequests(username, request); /*Collections.singletonList((BloodRequest) )*/

            if (bloodRequests == null || bloodRequests.isEmpty()) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("No blood requests were created"));

            }

            return ResponseEntity.ok(bloodRequests);

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));


        }
    }

    //@PreAuthorize("hasRole(T(org.springframework.blood_link_server.models.enumerations.UserRole).BLOODBANK.name())")
    @GetMapping("get-pending-bloodRequests-by-bloodbank/id={bankId}/status={status}")
    public ResponseEntity<?> getPendingBloodRequest(@PathVariable UUID bankId, @PathVariable RequestStatus status) {
        try{
            List<BloodRequest> bloodRequests = requestService.getPendingBloodRequestsByIdAndStatus(bankId, status);

            if(bloodRequests == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("No blood requests with that status found "));
            }
            return ResponseEntity.ok(bloodRequests);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("process-request/{requestId}")
    public ResponseEntity<?> processingRequest(HttpServletRequest httpServlet, @PathVariable UUID requestId) {
        try {
            String username = getUsername(httpServlet);
            return ResponseEntity.ok(requestService.processingBloodRequest(username, requestId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    private String getUsername(HttpServletRequest http) {
        final String authHeader = http.getHeader("Authorization");
        var jwt = authHeader.substring(7);
        return jwtService.extractUsername(jwt);
    }


}
