package cn.benbenedu.gravity.auth.service;

import cn.benbenedu.gravity.auth.model.SundialUserDetails;
import cn.benbenedu.gravity.auth.repository.AccountRepository;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary
public class SundialUserDetailsService
        implements UserDetailsService {

    private AccountRepository accountRepository;

    public SundialUserDetailsService(
            AccountRepository accountRepository) {

        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final var account = ObjectId.isValid(username) ?
                accountRepository.findById(username) :
                accountRepository.findByIdNumberOrMobileOrEmail(username, username, username);   // TODO

        return account
                .map(SundialUserDetails::of)
                .orElseThrow(() ->
                        new UsernameNotFoundException("No Account with requested username: " + username));
    }
}
