package cn.benbenedu.gravity.auth.configuration;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
public class CachingConfig {

    @Bean
    public CaffeineCacheManager cacheManager(CacheProperties cacheProperties) {

        final var cacheManager = new CaffeineCacheManager(
                cacheProperties.getCacheNames().toArray(new String[0]));
        cacheManager.setCacheSpecification(
                cacheProperties.getCaffeine().getSpec());

        return cacheManager;
    }
}
