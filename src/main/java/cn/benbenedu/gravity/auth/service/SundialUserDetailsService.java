package cn.benbenedu.gravity.auth.service;

import cn.benbenedu.gravity.auth.model.SundialUserDetails;
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

    private AccountService accountService;

    public SundialUserDetailsService(
            AccountService accountService) {

        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return getUserDetailsByUsername(username);
    }

    private SundialUserDetails getUserDetailsByUsername(String username)
            throws UsernameNotFoundException {

        if (ObjectId.isValid(username)) {
            return Optional.ofNullable(accountService.getUserDetailsById(username))
                    .orElseThrow(() ->
                            new UsernameNotFoundException("No Account with provided id: " + username));
        } else if (MobileUtility.isWellFormedMobileNumber(username)) {
            return accountService.getUserDetailsByMobile(username);
        } else if (EmailUtility.isWellFormedEmailAddress(username)) {
            return accountService.getUserDetailsByEmail(username);
        } else if (IdentityUtility.isWellFormedIdNumber(username)) {
            return accountService.getUserDetailsByIdNumber(username);
        } else {
            throw new UsernameNotFoundException(
                    "Unrecognized style within provided username: " + username);
        }
    }
}
