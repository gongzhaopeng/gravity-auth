package cn.benbenedu.gravity.auth.repository;

import cn.benbenedu.gravity.auth.model.SundialUserAuthParams;
import cn.benbenedu.sundial.account.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository
        extends MongoRepository<Account, String> {

    Optional<SundialUserAuthParams> findUserAuthParamsById(String id);

    Optional<SundialUserAuthParams> findUserAuthParamsByMobile(String mobile);

    Optional<SundialUserAuthParams> findUserAuthParamsByEmail(String email);

    Optional<SundialUserAuthParams> findUserAuthParamsByIdNumber(String idNumber);
}
