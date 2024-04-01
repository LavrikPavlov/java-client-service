package ru.kazan.clientservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kazan.clientservice.dto.client.*;
import ru.kazan.clientservice.exception.ApplicationException;
import ru.kazan.clientservice.exception.ExceptionEnum;
import ru.kazan.clientservice.service.ClientService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;


    @GetMapping("/info")
    public ResponseEntity<ResponseInfoDto> getInfo(@RequestParam(required = false) String type,
                                                   @RequestHeader("Authorization") String token){
        if(type == null || type.isEmpty())
            type = "short";

        return switch (type.toLowerCase()) {
            case "short" -> ResponseEntity.ok(clientService.getShortInfoClient(token));
            case "full" -> ResponseEntity.ok(clientService.getFullInfoClient(token));
            default -> throw new ApplicationException(ExceptionEnum.BAD_REQUEST);
        };
    }

    @PatchMapping("/edit/email")
    public ResponseEntity<Void> editEmail(@RequestBody RequestEditEmailDto request,
                                          @RequestHeader("Authorization") String token,
                                          @RequestHeader("Session") String sessionToken){
        clientService.changeEmail(request, token, sessionToken);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/edit/mobile-phone")
    public ResponseEntity<Void> editMobilePhone(@RequestBody RequestEditMobilePhoneDto request,
                                                @RequestHeader("Authorization") String token,
                                                @RequestHeader("Session") String sessionToken){
        clientService.changeMobilePhone(request, token, sessionToken);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/edit/address")
    public ResponseEntity<Void> addNewAddress(@RequestBody NewAddressDto request,
                                              @RequestHeader("Authorization") String token){
        clientService.addNewAddress(request, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/edit/delete/address")
    public ResponseEntity<Void> deleteAddress(@RequestBody DeleteAddressDto request,
                                              @RequestHeader("Authorization") String token){
        clientService.deleteAddress(request, token);
        return ResponseEntity.ok().build();
    }
}
