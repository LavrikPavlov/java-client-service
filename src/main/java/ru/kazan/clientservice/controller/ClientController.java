package ru.kazan.clientservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class ClientController {

    @GetMapping("/info")
    public ResponseEntity<Void> getInfo(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/info")
    public ResponseEntity<Void> editInfo(){
        return ResponseEntity.ok().build();
    }
}
