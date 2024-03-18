package ru.kazan.clientservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kazan.clientservice.dto.client.NewAddressDto;
import ru.kazan.clientservice.dto.client.RequestEditEmailDto;
import ru.kazan.clientservice.dto.client.RequestEditMobilePhoneDto;
import ru.kazan.clientservice.dto.client.RequestInfoDto;
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
    public ResponseEntity<?> getInfo(@RequestBody RequestInfoDto dto){
        if(dto.getType() == null)
            dto.setType("short");

        return switch (dto.getType()) {
            case "short" -> ResponseEntity.ok(clientService.getShortInfoClient(dto));
            case "full" -> ResponseEntity.ok(clientService.getFullInfoClient(dto));
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
