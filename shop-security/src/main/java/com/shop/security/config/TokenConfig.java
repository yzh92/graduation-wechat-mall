package com.shop.security.config;

import com.shop.security.constants.SecurityConstants;
import com.shop.security.util.ShopTokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import java.util.Collections;

/**
 * @author yzh
 */
@Configuration
public class TokenConfig {


    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private TokenEnhancer tokenEnhancer;

    @Bean
    public TokenStore tokenStore() {
        RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
        tokenStore.setPrefix(SecurityConstants.SHOP_OAUTH_PREFIX);
        return tokenStore;
    }


    @Primary
    @Bean
    @Lazy
    public AuthorizationServerTokenServices yamiTokenServices() {
        ShopTokenServices tokenServices = new ShopTokenServices();
        tokenServices.setTokenStore(tokenStore());
        //支持刷新token
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(true);
        tokenServices.setTokenEnhancer(tokenEnhancer);
        addUserDetailsService(tokenServices);
        return tokenServices;
    }

    private void addUserDetailsService(ShopTokenServices tokenServices) {
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<>(userDetailsService));
        tokenServices.setAuthenticationManager(new ProviderManager(Collections.singletonList(provider)));
    }

}
