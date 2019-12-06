package cn.benbenedu.gravity.auth.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Reference: org.springframework.security.oauth2.provider.client.BaseClientDetails
 * TODO
 */
@Data
public class SundialClientDetails implements ClientDetails {

    private String clientId;
    private String clientSecret;
    private Set<String> scope;
    private Set<String> resourceIds;
    private Set<String> authorizedGrantTypes;
    private Set<String> registeredRedirectUri;
    private Set<String> autoApproveScopes;
    private List<GrantedAuthority> authorities;
    private Integer accessTokenValiditySeconds;
    private Integer refreshTokenValiditySeconds;
    private Map<String, Object> additionalInformation;


    /**
     * TODO
     *
     * @param clientAuthParams
     * @return
     */
    public static SundialClientDetails of(SundialClientAuthParams clientAuthParams) {

        final var clientDetails = new SundialClientDetails();
        clientDetails.setClientId(clientAuthParams.getClientId());
        clientDetails.setClientSecret(clientAuthParams.getClientSecret());
        clientDetails.setScope(clientAuthParams.getScope());
        clientDetails.setResourceIds(clientAuthParams.getResourceIds());
        clientDetails.setAuthorizedGrantTypes(clientAuthParams.getAuthorizedGrantTypes());
        clientDetails.setRegisteredRedirectUri(clientAuthParams.getRegisteredRedirectUris());
        clientDetails.setAutoApproveScopes(clientAuthParams.getAutoApproveScopes());
        clientDetails.setAuthorities(clientAuthParams.getAuthorities());
        clientDetails.setAccessTokenValiditySeconds(clientAuthParams.getAccessTokenValiditySeconds());
        clientDetails.setRefreshTokenValiditySeconds(clientAuthParams.getRefreshTokenValiditySeconds());

        return clientDetails;
    }

    @Override
    public boolean isAutoApprove(String scope) {

        return Optional.ofNullable(autoApproveScopes)
                .map(autos -> autos.contains(scope))
                .orElse(false);
    }

    @Override
    public boolean isSecretRequired() {
        return true;
    }

    @Override
    public boolean isScoped() {
        return this.scope != null &&
                !this.scope.isEmpty();
    }
}
