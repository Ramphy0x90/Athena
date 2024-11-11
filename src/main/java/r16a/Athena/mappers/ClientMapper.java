package r16a.Athena.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import r16a.Athena.models.Client;
import r16a.Athena.models.dto.ClientRestricted;

import java.sql.Blob;
import java.util.Base64;

@Component
public class ClientMapper {
    private final ModelMapper modelMapper;

    public ClientMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ClientRestricted clientToRestricted(Client client) {
        ClientRestricted clientRestricted = modelMapper.map(client, ClientRestricted.class);

        if(client.getIcon() != null && client.getIcon().length != 0) {
            String iconBase64 = Base64.getEncoder().encodeToString(client.getIcon());
            clientRestricted.setIcon(iconBase64);
        }

        return clientRestricted;
    }
}
