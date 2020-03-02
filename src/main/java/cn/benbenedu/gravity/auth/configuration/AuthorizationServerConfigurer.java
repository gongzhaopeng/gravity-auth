package cn.benbenedu.gravity.auth.configuration;

import cn.benbenedu.gravity.auth.model.SundialUserDetails;
import cn.benbenedu.gravity.auth.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfigurer
        extends AuthorizationServerConfigurerAdapter {

    private ClientDetailsService clientDetailsService;
    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private RedisConnectionFactory redisConnectionFactory;

    private ClientRepository clientRepository;

    @Autowired
    public AuthorizationServerConfigurer(
            ClientDetailsService clientDetailsService,
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            RedisConnectionFactory redisConnectionFactory,
            ClientRepository clientRepository) {

        this.clientDetailsService = clientDetailsService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.redisConnectionFactory = redisConnectionFactory;

        this.clientRepository = clientRepository;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients)
            throws Exception {

        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
            throws Exception {

        final var tokenStore = tokenStore();

        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenStore(tokenStore);

        endpoints.accessTokenConverter(accessTokenConverter());

        endpoints.tokenServices(customTokenServices(tokenStore));
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security)
            throws Exception {

        security.passwordEncoder(NoOpPasswordEncoder.getInstance());

        security.allowFormAuthenticationForClients();
        security.checkTokenAccess("isAuthenticated()");
    }

    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Bean
    public AccessTokenConverter accessTokenConverter() {

        final var accessTokenConverter = new DefaultAccessTokenConverter() {

            private static final String CLIENT = "client";
            private static final String CLIENT_OWNER = "owner";

            @Override
            public Map<String, ?> convertAccessToken(
                    OAuth2AccessToken token,
                    OAuth2Authentication authentication) {

                final var response =
                        (Map<String, Object>) super.convertAccessToken(token, authentication);

                clientRepository.findClientAuthParamsByClientId(
                        authentication.getOAuth2Request().getClientId())
                        .ifPresent(params -> {

                                    final var clientInfo = new HashMap<String, Object>();
                                    if (params.getOwner() != null) {
                                        clientInfo.put(CLIENT_OWNER, params.getOwner().getId());
                                    }

                                    response.put(CLIENT, clientInfo);
                                }
                        );

                return response;
            }
        };

        accessTokenConverter.setUserTokenConverter(userAuthenticationConverter());

        return accessTokenConverter;
    }

    @Bean
    public UserAuthenticationConverter userAuthenticationConverter() {

        return new DefaultUserAuthenticationConverter() {

            private static final String USER = "user";
            private static final String USER_TYPE = "type";
            private static final String USER_NAME = "name";
            private static final String USER_NICKNAME = "nickname";

            @Override
            public Map<String, ?> convertUserAuthentication(
                    Authentication authentication) {

                final var response =
                        (Map<String, Object>) super.convertUserAuthentication(authentication);

                final var sundialUserDetails =
                        (SundialUserDetails) authentication.getPrincipal();

                final var userInfo = new HashMap<String, Object>();
                userInfo.put(USER_TYPE, sundialUserDetails.getType());
                userInfo.put(USER_NAME, sundialUserDetails.getName());
                userInfo.put(USER_NICKNAME, sundialUserDetails.getNickname());

                response.put(USER, userInfo);

                return response;
            }
        };
    }

    private AuthorizationServerTokenServices customTokenServices(TokenStore tokenStore) {

        final var tokenServices = new CustomTokenServices();

        tokenServices.setTokenStore(tokenStore);
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(true);
        tokenServices.setClientDetailsService(this.clientDetailsService);
        tokenServices.setTokenEnhancer(null);
        addUserDetailsService(tokenServices, this.userDetailsService);

        return tokenServices;
    }

    private void addUserDetailsService(CustomTokenServices tokenServices, UserDetailsService userDetailsService) {
        if (userDetailsService != null) {
            PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
            provider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken>(
                    userDetailsService));
            tokenServices
                    .setAuthenticationManager(new ProviderManager(Arrays.<AuthenticationProvider>asList(provider)));
        }
    }
}
