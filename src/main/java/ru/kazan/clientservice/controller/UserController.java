package ru.kazan.clientservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kazan.clientservice.dto.jwt.JwtRequest;
import ru.kazan.clientservice.dto.jwt.JwtResponse;
import ru.kazan.clientservice.dto.user.RegistrationClientDto;
import ru.kazan.clientservice.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/token")
    public ResponseEntity<JwtResponse> refreshToken(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtRequest> auth(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reg")
    public ResponseEntity<Void> registration(@RequestBody @Valid RegistrationClientDto dto){
        userService.createNewClient(dto);
        return ResponseEntity.ok().build();
    }


}
