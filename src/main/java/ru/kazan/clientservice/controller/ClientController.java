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
                                                   @RequestParam String clientId){
        if(type == null || type.isEmpty())
            type = "short";

        return switch (type.toLowerCase()) {
            case "short" -> ResponseEntity.ok(clientService.getShortInfoClient(clientId));
            case "full" -> ResponseEntity.ok(clientService.getFullInfoClient(clientId));
            default -> throw new ApplicationException(ExceptionEnum.BAD_REQUEST);
        };
    }

    @PatchMapping("/edit/email")
    public ResponseEntity<Void> editEmail(@RequestBody RequestEditEmailDto request){
        clientService.changeEmail(request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/edit/mobile-phone")
    public ResponseEntity<Void> editMobilePhone(@RequestBody RequestEditMobilePhoneDto request){
        clientService.changeMobilePhone(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/edit/address")
    public ResponseEntity<Void> addNewAddress(@RequestBody NewAddressDto request){
        clientService.addNewAddress(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/edit/delete/address")
    public ResponseEntity<Void> deleteAddress(@RequestBody DeleteAddressDto request){
        clientService.deleteAddress(request);
        return ResponseEntity.ok().build();
    }
}
