package ru.kazan.clientservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kazan.clientservice.dto.jwt.JwtRequest;

@RestController
@RequestMapping("/login")
public class AuthenticationController {


    @PostMapping("/auth")
    public ResponseEntity<JwtRequest> auth(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/token")
    public ResponseEntity<Void> verify(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh/password")
    public ResponseEntity<Void> changePassword(){
        return ResponseEntity.ok().build();
    }

}
