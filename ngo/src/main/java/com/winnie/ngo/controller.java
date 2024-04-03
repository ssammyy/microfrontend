package com.winnie.ngo;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class controller {
    @GetMapping("/")
    public ResponseEntity<?> getData(){
        return new ResponseEntity<>(null, HttpStatusCode.valueOf(20));
    }
}
