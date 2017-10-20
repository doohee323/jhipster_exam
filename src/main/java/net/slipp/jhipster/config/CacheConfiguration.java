package net.slipp.jhipster.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(net.slipp.jhipster.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(net.slipp.jhipster.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(net.slipp.jhipster.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(net.slipp.jhipster.domain.SocialUserConnection.class.getName(), jcacheConfiguration);
            cm.createCache(net.slipp.jhipster.domain.Customer.class.getName(), jcacheConfiguration);
            cm.createCache(net.slipp.jhipster.domain.Orders.class.getName(), jcacheConfiguration);
            cm.createCache(net.slipp.jhipster.domain.OrderItems.class.getName(), jcacheConfiguration);
            cm.createCache(net.slipp.jhipster.domain.Product.class.getName(), jcacheConfiguration);
            cm.createCache(net.slipp.jhipster.domain.Product.class.getName() + ".orderItems", jcacheConfiguration);
            cm.createCache(net.slipp.jhipster.domain.Product.class.getName() + ".cartItems", jcacheConfiguration);
            cm.createCache(net.slipp.jhipster.domain.Product.class.getName() + ".tags", jcacheConfiguration);
            cm.createCache(net.slipp.jhipster.domain.CartItems.class.getName(), jcacheConfiguration);
            cm.createCache(net.slipp.jhipster.domain.Tag.class.getName(), jcacheConfiguration);
            cm.createCache(net.slipp.jhipster.domain.Tag.class.getName() + ".products", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
