package ru.kazan.clientservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kazan.clientservice.dto.jwt.JwtResponse;

@RestController
@RequestMapping("/session")
public class SessionController {

    @PostMapping("/token")
    public ResponseEntity<JwtResponse> refreshToken(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify")
    public ResponseEntity<JwtResponse> verify(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/new")
    public ResponseEntity<Void> setPassword(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/change")
    public ResponseEntity<Void> changePassword(){
        return ResponseEntity.ok().build();
    }

}
