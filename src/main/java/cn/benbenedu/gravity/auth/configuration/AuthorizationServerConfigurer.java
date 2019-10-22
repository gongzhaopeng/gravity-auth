package cn.benbenedu.gravity.auth.configuration;

import cn.benbenedu.gravity.auth.model.Client;
import cn.benbenedu.gravity.auth.model.ClientState;
import cn.benbenedu.gravity.auth.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.Set;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfigurer
        extends AuthorizationServerConfigurerAdapter {

    private ClientRepository clientRepository;
    private ClientDetailsService clientDetailsService;
    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    public AuthorizationServerConfigurer(
            ClientRepository clientRepository,
            ClientDetailsService clientDetailsService,
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            RedisConnectionFactory redisConnectionFactory) {

        this.clientRepository = clientRepository;
        this.clientDetailsService = clientDetailsService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients)
            throws Exception {

        initSundialCoreClient();

        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
            throws Exception {

        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenStore(tokenStore());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security)
            throws Exception {

        security.passwordEncoder(NoOpPasswordEncoder.getInstance());

        security.allowFormAuthenticationForClients();
        security.checkTokenAccess("permitAll()");
    }

    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    private void initSundialCoreClient() {

        final var coreClientId = "__SUNDIAL_CORE_CLIENT__";

        if (clientRepository.existsById(coreClientId)) {
            return;
        }

        final var coreClient = new Client();
        coreClient.setClientId(coreClientId);
        /**
         * TODO External the configuration of Sundial-core-client-secret.
         */
        coreClient.setClientSecret("LET_THERE_BE_LIGHT");
        coreClient.setAuthorizedGrantTypes(Set.of(
                "refresh_token",
                "password",
                "client_credentials",
                "authorization_code"
        ));
        coreClient.setScope(Set.of(
                "client:inner",
                "client:partner",
                "client:public"
        ));
        coreClient.setRegisteredRedirectUris(Set.of(
                "https://www.baidu.com"
        ));
        coreClient.setState(ClientState.Active);

        clientRepository.save(coreClient);
    }
}
