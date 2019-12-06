package cn.benbenedu.gravity.auth.service;

import cn.benbenedu.gravity.auth.model.SundialUserAuthParams;
import cn.benbenedu.gravity.auth.model.SundialUserDetails;
import cn.benbenedu.gravity.auth.repository.AccountRepository;
import cn.benbenedu.utility.EmailUtility;
import cn.benbenedu.utility.IdentityUtility;
import cn.benbenedu.utility.MobileUtility;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

        final var userAuthParams = getUserAuthParamsByUsername(username);

        return userAuthParams
                .map(SundialUserDetails::of)
                .orElseThrow(() ->
                        new UsernameNotFoundException("No Account with provided username: " + username));
    }

    private Optional<SundialUserAuthParams> getUserAuthParamsByUsername(String username)
            throws UsernameNotFoundException {

        if (ObjectId.isValid(username)) {
            return accountRepository.findUserAuthParamsById(username);
        } else if (MobileUtility.isWellFormedMobileNumber(username)) {
            return accountRepository.findUserAuthParamsByMobile(username);
        } else if (EmailUtility.isWellFormedEmailAddress(username)) {
            return accountRepository.findUserAuthParamsByEmail(username);
        } else if (IdentityUtility.isWellFormedIdNumber(username)) {
            return accountRepository.findUserAuthParamsByIdNumber(username);
        } else {
            throw new UsernameNotFoundException(
                    "Unrecognized style within provided username: " + username);
        }
    }
}
