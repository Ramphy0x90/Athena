package r16a.Athena.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import r16a.Athena.models.dto.ClientRegister;
import r16a.Athena.models.dto.ClientRestricted;
import r16a.Athena.services.ClientService;

@Slf4j
@RestController
@RequestMapping("/api/v1/client")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/register")
    public ResponseEntity<ClientRestricted> registerClient(@RequestBody ClientRegister newClient) {
        log.info("ClientController: Register client {}", newClient.getName());
        return new ResponseEntity<>(clientService.register(newClient), HttpStatus.ACCEPTED);
    }
}
