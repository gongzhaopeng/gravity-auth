package cn.benbenedu.gravity.auth.repository;

import cn.benbenedu.gravity.auth.model.SundialClientAuthParams;
import cn.benbenedu.sundial.account.model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository
        extends MongoRepository<Client, String> {

    Optional<SundialClientAuthParams> findClientAuthParamsByClientId(
            String clientId);
}
