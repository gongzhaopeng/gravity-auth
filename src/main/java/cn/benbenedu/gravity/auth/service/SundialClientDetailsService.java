package cn.benbenedu.gravity.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@Primary
public class SundialClientDetailsService
        implements ClientDetailsService {

    private ClientService clientService;

    public SundialClientDetailsService(
            ClientService clientService) {

        this.clientService = clientService;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId)
            throws ClientRegistrationException {

        return Optional.ofNullable(
                clientService.getClientDetailsByClientId(clientId))
                .orElseThrow(() ->
                        new NoSuchClientException("No client with provided id: " + clientId));
    }
}
