package ru.kazan.clientservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticationController {


    @PostMapping("/auth")
    public ResponseEntity<Void> auth(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/registration")
    public ResponseEntity<Void> reg(){
        return ResponseEntity.ok().build();
    }

}
