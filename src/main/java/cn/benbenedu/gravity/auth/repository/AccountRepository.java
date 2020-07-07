package cn.benbenedu.gravity.auth.repository;

import cn.benbenedu.sundial.account.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository
        extends MongoRepository<Account, String> {

    Optional<Account> findByMobile(String mobile);

    Optional<Account> findByEmail(String email);

    Optional<Account> findByIdNumber(String idNumber);

    Optional<Account> findByWechatUnionid(String wechatUnionid);

    Optional<Account> findByByteDanceOpenid(String byteDanceOpenid);
}
