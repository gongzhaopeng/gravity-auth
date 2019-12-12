package cn.benbenedu.gravity.auth.configuration;

import cn.benbenedu.gravity.auth.model.SundialUserDetails;
import cn.benbenedu.gravity.auth.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
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
import org.springframework.security.oauth2.provider.endpoint.WhitelabelApprovalEndpoint;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

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

        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenStore(tokenStore());

        endpoints.accessTokenConverter(accessTokenConverter());
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
                        .map(params ->
                                response.put(CLIENT, Map.of(
                                        CLIENT_OWNER, params.getOwner().getId()
                                ))
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

                response.put(USER, Map.of(
                        USER_TYPE, sundialUserDetails.getType(),
                        USER_NAME, sundialUserDetails.getName(),
                        USER_NICKNAME, sundialUserDetails.getNickname()
                ));

                return response;
            }
        };
    }
}
