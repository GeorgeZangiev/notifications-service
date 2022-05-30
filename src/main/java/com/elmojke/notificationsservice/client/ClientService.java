package com.elmojke.notificationsservice.client;

import com.elmojke.notificationsservice.exception.ClientNotFoundException;
import com.elmojke.notificationsservice.exception.PhoneNumberHasBeenTakenException;
import com.elmojke.notificationsservice.requests.ClientRegistrationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public void registerClient(ClientRegistrationRequest request) {
        if (clientRepository.existsByPhoneNumber(request.phoneNumber()) == true){
            log.info("phone number " + request.phoneNumber() + " is already registered");
            throw new PhoneNumberHasBeenTakenException();
        }
        else {
            Client client = Client.builder()
                    .phoneNumber(request.phoneNumber())
                    .operatorCode(request.phoneNumber().substring(1, 4))
                    .clientTag(request.clientTag())
                    .timeZone(request.timeZone())
                    .build();

            clientRepository.save(client);
        }
    }

    public void deleteClient(Integer clientId) {
        if(!clientRepository.existsById(clientId)) {
            log.info("client with id " + clientId + " does not exist");
            throw new ClientNotFoundException();
        }
        else clientRepository.deleteById(clientId);
    }

    public ResponseEntity<Client> updateClient(Integer clientId, Client clientDetails) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> {
                    log.info("client with id " + clientId + " does not exist");
                    return new ClientNotFoundException();
                });
        if (client.getPhoneNumber().equals(clientDetails.getPhoneNumber())
                || !clientRepository.existsByPhoneNumber(clientDetails.getPhoneNumber())){
            client.setPhoneNumber(clientDetails.getPhoneNumber());
            client.setOperatorCode(clientDetails.getPhoneNumber().substring(1, 4));
            client.setClientTag(clientDetails.getClientTag());
            client.setTimeZone(clientDetails.getTimeZone());
            Client updatedClient = clientRepository.save(client);
            return ResponseEntity.ok(updatedClient);
        }
        else{
            log.info("phone number " + clientDetails.getPhoneNumber() + " is already registered");
            throw new PhoneNumberHasBeenTakenException();
        }
    }
}
