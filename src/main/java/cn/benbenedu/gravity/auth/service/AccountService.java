package cn.benbenedu.gravity.auth.service;

import cn.benbenedu.gravity.auth.repository.AccountRepository;
import cn.benbenedu.sundial.account.model.Account;
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
    public Account getAccountById(String id) {

        return accountRepository.findById(id)
                .orElse(null);
    }

    @CachePut(key = "#result.id")
    public Account getAccountByMobile(String mobile)
            throws UsernameNotFoundException {

        return accountRepository.findByMobile(mobile)
                .orElseThrow(() ->
                        new UsernameNotFoundException("No Account with provided mobile: " + mobile));
    }

    @CachePut(key = "#result.id")
    public Account getAccountByEmail(String email)
            throws UsernameNotFoundException {

        return accountRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("No Account with provided email: " + email));
    }

    @CachePut(key = "#result.id")
    public Account getAccountByIdNumber(String idNumber)
            throws UsernameNotFoundException {

        return accountRepository.findByIdNumber(idNumber)
                .orElseThrow(() ->
                        new UsernameNotFoundException("No Account with provided idNumber: " + idNumber));
    }

    @CachePut(key = "#result.id")
    public Account getAccountByWechatUnionid(String wechatUnionid)
            throws UsernameNotFoundException {

        return accountRepository.findByWechatUnionid(wechatUnionid)
                .orElseThrow(() ->
                        new UsernameNotFoundException("No Account with provided wechat-unionid: " + wechatUnionid));
    }
}
