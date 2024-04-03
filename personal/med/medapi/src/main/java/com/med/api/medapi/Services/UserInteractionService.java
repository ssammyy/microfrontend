package com.med.api.medapi.Services;

import com.google.gson.*;
import com.med.api.medapi.Requests.UserRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserInteractionService {

    @Autowired
    Gson gson;
    JsonObject response = new JsonObject();

    public ResponseEntity<?> firstSymptom(UserRequest userRequest) {
        response = new JsonObject();

        List<String> matchedSymptoms = new ArrayList<>();
        String[] symptomArray = {
                "headache",
                "itching",
                "vomiting",
                "head rush",
                "head",
                "stomach ache",
                "bloating",
                "back-itching"
        };
        String userSymptom = userRequest.getSymptom();


        for (String symptom : symptomArray) {
            if (symptom.contains(userSymptom)) {
                matchedSymptoms.add(symptom);
            }
        }
        String responseMessage;
        String jsonResponse = null;
        if (matchedSymptoms.size() > 0) {
            jsonResponse = gson.toJson(matchedSymptoms);
            JsonElement jsonElement = JsonParser.parseString(jsonResponse);

            response.add("symptoms", jsonElement);
            response.addProperty("message", "similar symptoms found");


        } else {
            response.addProperty("message", "no symptoms found, please key in an appropriate symptom");
        }


        return new ResponseEntity<>(gson.toJson(response) , HttpStatus.OK);
    }


    public ResponseEntity<?> firstDiagnosis(UserRequest userRequest){
        String actualSymptom = userRequest.getActualSymptom();
        List<JsonObject> diseaseObjects = new ArrayList<>();

// Diseases associated with symptoms
        String[] cholera = {"headache", "vomiting", "stomach upset"};
        String[] amoeba = {"stomach ache", "back pains"};
        String[] tb = {"itching", "coughing","headache"};

// Check for each disease
        if (Arrays.asList(cholera).contains(actualSymptom)) {
            JsonObject diseaseObject = new JsonObject();
            diseaseObject.addProperty("disease", "cholera");
            diseaseObject.add("symptoms", gson.toJsonTree(Arrays.asList(cholera)));
            diseaseObjects.add(diseaseObject);
        }
        if (Arrays.asList(amoeba).contains(actualSymptom)) {
            JsonObject diseaseObject = new JsonObject();
            diseaseObject.addProperty("disease", "amoeba");
            diseaseObject.add("symptoms", gson.toJsonTree(Arrays.asList(amoeba)));
            diseaseObjects.add(diseaseObject);
        }
        if (Arrays.asList(tb).contains(actualSymptom)) {
            JsonObject diseaseObject = new JsonObject();
            diseaseObject.addProperty("disease", "TB");
            diseaseObject.add("symptoms", gson.toJsonTree(Arrays.asList(tb)));
            diseaseObjects.add(diseaseObject);
        }

        if (!diseaseObjects.isEmpty()) {
            return new ResponseEntity<>(gson.toJson(diseaseObjects), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No matching disease found for the symptom.", HttpStatus.OK);
        }



    }
}
