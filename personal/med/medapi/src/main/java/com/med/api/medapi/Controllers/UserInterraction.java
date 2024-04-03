package com.med.api.medapi.Controllers;

import com.med.api.medapi.Requests.UserRequest;
import com.med.api.medapi.Services.UserInteractionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/med/")
@AllArgsConstructor
public class UserInterraction {

    private final UserInteractionService userInteractionService;

    @PostMapping("/symptom")
    public ResponseEntity<?> firstSysmptom(@RequestBody UserRequest userRequest){

        return userInteractionService.firstSymptom(userRequest);
    }
    @PostMapping("/actual/symptom")
    public ResponseEntity<?> actualSymptom(@RequestBody UserRequest userRequest){

        return userInteractionService.firstDiagnosis(userRequest);
    }
}
