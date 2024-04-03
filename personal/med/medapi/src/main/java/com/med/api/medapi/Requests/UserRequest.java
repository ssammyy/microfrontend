package com.med.api.medapi.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String symptom;
    private String actualSymptom;
    private String numberOfDays;
}
