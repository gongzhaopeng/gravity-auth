package cn.benbenedu.gravity.auth.service;

import cn.benbenedu.gravity.auth.model.SundialUserDetails;
import cn.benbenedu.sundial.account.model.Account;
import cn.benbenedu.utility.EmailUtility;
import cn.benbenedu.utility.IdentityUtility;
import cn.benbenedu.utility.MobileUtility;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Primary
public class SundialUserDetailsService
        implements UserDetailsService {

    private static final String WECHAT_UNIONID_PREFIX = "WECHAT#";

    private PasswordEncoder passwordEncoder;
    private AccountService accountService;

    public SundialUserDetailsService(
            PasswordEncoder passwordEncoder,
            AccountService accountService) {

        this.passwordEncoder = passwordEncoder;
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return getUserDetailsByUsername(username);
    }

    private SundialUserDetails getUserDetailsByUsername(String username)
            throws UsernameNotFoundException {

        Account account;
        if (ObjectId.isValid(username)) {
            account = Optional.ofNullable(accountService.getAccountById(username))
                    .orElseThrow(() ->
                            new UsernameNotFoundException("No Account with provided id: " + username));
        } else if (username.startsWith(WECHAT_UNIONID_PREFIX)) {
            final var wechatUnionid =
                    username.substring(WECHAT_UNIONID_PREFIX.length());
            account = accountService.getAccountByWechatUnionid(wechatUnionid);
            account.setPassword(
                    passwordEncoder.encode(account.getWechat().getToken()));
        } else if (MobileUtility.isWellFormedMobileNumber(username)) {
            account = accountService.getAccountByMobile(username);
        } else if (EmailUtility.isWellFormedEmailAddress(username)) {
            account = accountService.getAccountByEmail(username);
        } else if (IdentityUtility.isWellFormedIdNumber(username)) {
            account = accountService.getAccountByIdNumber(username);
        } else {
            throw new UsernameNotFoundException(
                    "Unrecognized style within provided username: " + username);
        }

        return SundialUserDetails.of(account);
    }
}
