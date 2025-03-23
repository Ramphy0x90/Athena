package r16a.Athena.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import r16a.Athena.models.dto.ClientRegister;
import r16a.Athena.models.dto.ClientRestricted;
import r16a.Athena.services.ClientService;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/client")
public class ClientController {
    private final ClientService clientService;

    @GetMapping("/all")
    public ResponseEntity<List<ClientRestricted>> getAllRestricted() {
        log.info("Get all clients restricted");
        return new ResponseEntity<>(clientService.getAllRestricted(), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClientRestricted> updateClient(@PathVariable Integer id, @RequestBody ClientRestricted client) {
        log.info("Update client");
        return new ResponseEntity<>(clientService.update(id, client), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<ClientRestricted> registerClient(@RequestBody ClientRegister newClient) {
        log.info("Register client {}", newClient.getName());
        return new ResponseEntity<>(clientService.register(newClient), HttpStatus.ACCEPTED);
    }
}
