package com.elmojke.notificationsservice.client;

import com.elmojke.notificationsservice.requests.ClientRegistrationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/clients")
@AllArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public List<Client> getAllClients() {
        log.info("getting all clients");
        return clientService.getAllClients();
    }

    @PostMapping("/register-client")
    public void registerClient(@RequestBody ClientRegistrationRequest clientRegistrationRequest) {
        log.info("new client registration {}", clientRegistrationRequest);
        clientService.registerClient(clientRegistrationRequest);
    }

    @DeleteMapping(path = "{clientId}")
    public void deleteClient(
            @PathVariable("clientId") Integer clientId) {
        log.info("deleting client with id: {}", clientId);
        clientService.deleteClient(clientId);
    }

    @PutMapping(path = "{clientId}")
    public void updateClient(
            @PathVariable("clientId") Integer clientId,
            @RequestBody Client clientDetails){
        log.info("updating client with id: {}", clientId);
        clientService.updateClient(clientId, clientDetails);
    }
}
