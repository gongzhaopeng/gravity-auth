package cn.benbenedu.gravity.auth.service;

import cn.benbenedu.gravity.auth.model.SundialClientDetails;
import cn.benbenedu.sundial.account.model.ClientState;
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

        return getClientDetailsByClientId(clientId)
                .orElseThrow(() ->
                        new NoSuchClientException("No client with provided id: " + clientId));
    }

    private Optional<SundialClientDetails> getClientDetailsByClientId(String clientId) {

        return Optional.ofNullable(clientService.getClientByClientId(clientId))
                .filter(client ->
                        client.getState() == ClientState.Active)
                .map(SundialClientDetails::of);
    }
}
