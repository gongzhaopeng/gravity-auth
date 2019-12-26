package cn.benbenedu.gravity.auth.service;

import cn.benbenedu.gravity.auth.model.SundialUserDetails;
import cn.benbenedu.gravity.auth.repository.AccountRepository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "accounts")
public class AccountService {

    private AccountRepository accountRepository;

    public AccountService(
            AccountRepository accountRepository) {

        this.accountRepository = accountRepository;
    }

    @Cacheable
    public SundialUserDetails getUserDetailsById(String id) {

        return accountRepository.findUserAuthParamsById(id)
                .map(SundialUserDetails::of)
                .orElse(null);
    }

    @CachePut(key = "#result.id")
    public SundialUserDetails getUserDetailsByMobile(String mobile)
            throws UsernameNotFoundException {

        return accountRepository.findUserAuthParamsByMobile(mobile)
                .map(SundialUserDetails::of)
                .orElseThrow(() ->
                        new UsernameNotFoundException("No Account with provided mobile: " + mobile));
    }

    @CachePut(key = "#result.id")
    public SundialUserDetails getUserDetailsByEmail(String email)
            throws UsernameNotFoundException {

        return accountRepository.findUserAuthParamsByEmail(email)
                .map(SundialUserDetails::of)
                .orElseThrow(() ->
                        new UsernameNotFoundException("No Account with provided email: " + email));
    }

    @CachePut(key = "#result.id")
    public SundialUserDetails getUserDetailsByIdNumber(String idNumber)
            throws UsernameNotFoundException {

        return accountRepository.findUserAuthParamsByIdNumber(idNumber)
                .map(SundialUserDetails::of)
                .orElseThrow(() ->
                        new UsernameNotFoundException("No Account with provided idNumber: " + idNumber));
    }
}
