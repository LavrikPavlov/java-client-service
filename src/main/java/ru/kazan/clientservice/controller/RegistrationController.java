package ru.kazan.clientservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @PostMapping
    public ResponseEntity<Void> reg(){
        return ResponseEntity.ok().build();
    }

    @PostMapping()
    public ResponseEntity<Void> verify(){
        return ResponseEntity.ok().build();
    }



}
