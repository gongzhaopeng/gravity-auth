package cn.benbenedu.gravity.auth.service;

import cn.benbenedu.gravity.auth.model.SundialClientDetails;
import cn.benbenedu.gravity.auth.repository.ClientRepository;
import cn.benbenedu.sundial.account.model.ClientState;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "clients")
public class ClientService {

    private ClientRepository clientRepository;

    public ClientService(
            ClientRepository clientRepository) {

        this.clientRepository = clientRepository;
    }

    @Cacheable
    public SundialClientDetails getClientDetailsByClientId(String clientId) {

        return clientRepository.findClientAuthParamsByClientId(clientId)
                .filter(authParams ->
                        authParams.getState() == ClientState.Active)
                .map(SundialClientDetails::of)
                .orElse(null);
    }
}
