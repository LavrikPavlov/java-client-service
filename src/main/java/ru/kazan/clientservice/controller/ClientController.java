package ru.kazan.clientservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kazan.clientservice.dto.client.NewAddressDto;
import ru.kazan.clientservice.dto.client.RequestEditEmailDto;
import ru.kazan.clientservice.dto.client.RequestEditMobilePhoneDto;
import ru.kazan.clientservice.service.ClientService;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/info")
    public ResponseEntity<? extends Object> getInfo(@RequestParam String type, @RequestParam String clientId){
        if(type == null)
            type = "short";

        return switch (type) {
            case "short" -> ResponseEntity.ok(clientService.getShortInfoClient(clientId));
            case "full" -> ResponseEntity.ok(clientService.getFullInfoClient(clientId));
            default -> ResponseEntity.badRequest().body("Ошибка");
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
}
