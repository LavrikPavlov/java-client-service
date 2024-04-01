package ru.kazan.clientservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kazan.clientservice.dto.jwt.JwtSessionToken;
import ru.kazan.clientservice.dto.session.EmailWithCodeDtoImpl;
import ru.kazan.clientservice.dto.session.MobilePhoneCodeDtoImpl;
import ru.kazan.clientservice.dto.session.NewPasswordDto;
import ru.kazan.clientservice.dto.session.TypeCodeSendDto;
import ru.kazan.clientservice.exception.ApplicationException;
import ru.kazan.clientservice.exception.ExceptionEnum;
import ru.kazan.clientservice.service.SessionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/session")
public class SessionController {

    private final SessionService sessionService;


    @PostMapping("/verify")
    public ResponseEntity<Void> verifyCode(@Valid @RequestBody TypeCodeSendDto dto){
        if(dto.getType().isEmpty())
            throw new ApplicationException(ExceptionEnum.BAD_REQUEST);

        sessionService.verifyCode(dto);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify/email")
    public ResponseEntity<JwtSessionToken> verifyEmail(@RequestBody @Valid EmailWithCodeDtoImpl dto){
        return ResponseEntity.ok().body(sessionService.getSessionToken(dto, "email"));
    }

    @PostMapping("/verify/mobile")
    public ResponseEntity<JwtSessionToken> verifyMobilePhone(@RequestBody @Valid MobilePhoneCodeDtoImpl dto){
        return ResponseEntity.ok().body(sessionService.getSessionToken(dto, "mobile"));
    }

    @PatchMapping("/password/new")
    public ResponseEntity<Void> setPassword(@RequestHeader("Session") String token,
                                            @RequestBody NewPasswordDto dto){
        sessionService.setNewPasswordClient(dto,token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/change")
    public ResponseEntity<Void> changePassword(){
        return ResponseEntity.ok().build();
    }

}
