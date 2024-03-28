package ru.kazan.clientservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kazan.clientservice.dto.jwt.JwtRequest;

@RestController
@RequestMapping("/user")
public class UserController {


    @PostMapping("/auth")
    public ResponseEntity<JwtRequest> auth(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reg")
    public ResponseEntity<Void> registration(){
        return ResponseEntity.ok().build();
    }


}
