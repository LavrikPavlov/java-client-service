package ru.kazan.clientservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kazan.clientservice.dto.jwt.JwtResponse;
import ru.kazan.clientservice.dto.user.LoginWithPasswordDto;
import ru.kazan.clientservice.dto.user.RegistrationClientDto;
import ru.kazan.clientservice.exception.ApplicationException;
import ru.kazan.clientservice.exception.ExceptionEnum;
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
    public ResponseEntity<JwtResponse> auth(@RequestBody @Valid LoginWithPasswordDto dto){

        if(dto.getType() == null)
            throw new ApplicationException(ExceptionEnum.BAD_REQUEST, "Not Correct type of login");

        return switch (dto.getType()) {
            case EMAIL, MOBILE_PHONE -> ResponseEntity.ok()
                    .body(userService.loginWithEmailOrMobilePhone(dto.getLogin(), dto.getPassword()));
            case PASSPORT -> ResponseEntity.ok()
                    .body(userService.loginWithPassport(dto.getLogin(), dto.getPassword()));
        };
    }

    @PostMapping("/reg")
    public ResponseEntity<Void> registration(@RequestBody @Valid RegistrationClientDto dto){
        userService.createNewClient(dto);
        return ResponseEntity.ok().build();
    }


}
