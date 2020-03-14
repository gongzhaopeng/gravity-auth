package cn.benbenedu.gravity.auth.service;

import cn.benbenedu.gravity.auth.repository.ClientRepository;
import cn.benbenedu.sundial.account.model.Client;
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
    public Client getClientByClientId(String clientId) {

        return clientRepository.findByClientId(clientId).orElse(null);
    }
}
