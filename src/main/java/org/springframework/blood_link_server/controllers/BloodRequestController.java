package org.springframework.blood_link_server.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.blood_link_server.models.appl.BloodRequest;
import org.springframework.blood_link_server.models.dtos.requests.BloodDemandRequest;
import org.springframework.blood_link_server.models.dtos.responses.ErrorResponse;
import org.springframework.blood_link_server.services.interfaces.BloodRequestService;
import org.springframework.blood_link_server.services.interfaces.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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


        } /*catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e);
        }*/
    }

/*    @PostMapping("create1")
    public ResponseEntity<?> createRequest1(HttpServletRequest httpRequest, @RequestBody BloodDemandRequest request) {

        try{

            final String authHeader = httpRequest.getHeader("Authorization");
            var jwt = authHeader.substring(7);
            var username = jwtService.extractUsername(jwt);

            Set<BloodRequest> bloodRequests = requestService.sendRequests1(username, request); *//*Collections.singletonList((BloodRequest) )*//*

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
    }*/

}
