package cn.benbenedu.gravity.auth.repository;

import cn.benbenedu.sundial.account.model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository
        extends MongoRepository<Client, String> {

    Optional<Client> findByClientId(
            String clientId);
}
