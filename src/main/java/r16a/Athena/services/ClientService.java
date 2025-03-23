package r16a.Athena.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import r16a.Athena.exceptions.ClientAlreadyExistsException;
import r16a.Athena.exceptions.ClientNotFoundException;
import r16a.Athena.mappers.ClientMapper;
import r16a.Athena.models.Client;
import r16a.Athena.models.dto.ClientRegister;
import r16a.Athena.models.dto.ClientRestricted;
import r16a.Athena.repositories.ClientRepository;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service fo client operations. Register and validate clients.
 *
 * @author Ramphy Aquino Nova
 */
@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientRestricted getRestrictedById(Integer clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new ClientNotFoundException(clientId));
        return clientMapper.clientToRestricted(client);
    }

    public List<ClientRestricted> getAllRestricted() {
        return clientRepository.findAll().stream().map(clientMapper::clientToRestricted).collect(Collectors.toList());
    }

    public ClientRestricted register(ClientRegister newClient) {
        boolean clientExists = clientRepository.existsByName(newClient.getName());

        if(clientExists) {
            throw new ClientAlreadyExistsException(newClient.getName());
        }

        Client client = Client.builder()
                .name(newClient.getName())
                .description(newClient.getDescription())
                .icon(Base64.getDecoder().decode(newClient.getIcon()))
                .build();

        return clientMapper.clientToRestricted(clientRepository.save(client));
    }

    public ClientRestricted update(Integer clientId, ClientRestricted restricted) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new ClientNotFoundException(clientId));

        client.setName(restricted.getName());
        client.setDescription(restricted.getDescription());
        client.setBaseUrl(restricted.getBaseUrl());
        client.setPostAuthUrl(restricted.getPostAuthUrl());
        client.setIcon(Base64.getDecoder().decode(client.getIcon()));
        clientRepository.save(client);

        return clientMapper.clientToRestricted(client);
    }
}
