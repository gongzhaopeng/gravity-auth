package cn.benbenedu.gravity.auth.repository;

import cn.benbenedu.gravity.auth.model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository
        extends MongoRepository<Client, String> {
}
