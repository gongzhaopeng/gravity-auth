package cn.benbenedu.gravity.auth.model;

import cn.benbenedu.sundial.account.model.AccountBrief;
import cn.benbenedu.sundial.account.model.ClientState;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;

public interface SundialClientAuthParams {

    String getClientId();

    ClientState getState();

    AccountBrief getOwner();

    String getClientSecret();

    Set<String> getScope();

    Set<String> getResourceIds();

    Set<String> getAuthorizedGrantTypes();

    Set<String> getRegisteredRedirectUris();

    Set<String> getAutoApproveScopes();

    List<GrantedAuthority> getAuthorities();

    Integer getAccessTokenValiditySeconds();

    Integer getRefreshTokenValiditySeconds();
}
