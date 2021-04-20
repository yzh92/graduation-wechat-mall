package com.shop.security.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.*;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * Base implementation for token services using random UUID values for the access token and refresh token values. The
 * main extension point for customizations is the {@link TokenEnhancer} which will be called after the access and
 * refresh tokens have been generated but before they are stored.
 * <p>
 * Persistence is delegated to a {@code TokenStore} implementation and customization of the access token to a
 * {@link TokenEnhancer}.
 *
 * @author yzh
 */
public class ShopTokenServices implements AuthorizationServerTokenServices, ResourceServerTokenServices,
        ConsumerTokenServices, InitializingBean {

    // 默认三十天
    private int refreshTokenValiditySeconds = 60 * 60 * 24 * 30;

    // 默认十二个小时
    private int accessTokenValiditySeconds = 60 * 60 * 12;

    private boolean supportRefreshToken = false;

    private boolean reuseRefreshToken = true;

    private TokenStore tokenStore;

    private ClientDetailsService clientDetailsService;

    private TokenEnhancer accessTokenEnhancer;

    private AuthenticationManager authenticationManager;

    /**
     * Initialize these token services. If no random generator is set, one will be created.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(tokenStore, "tokenStore must be set");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) {

        OAuth2RefreshToken refreshToken = null;

        if (refreshToken == null) {
            refreshToken = createRefreshToken(authentication);
        }
        // But the refresh token itself might need to be re-issued if it has
        // expired.
        else if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
            ExpiringOAuth2RefreshToken expiring = (ExpiringOAuth2RefreshToken) refreshToken;
            if (System.currentTimeMillis() > expiring.getExpiration().getTime()) {
                refreshToken = createRefreshToken(authentication);
            }
        }

        OAuth2AccessToken accessToken = createAccessToken(authentication, refreshToken);
        tokenStore.storeAccessToken(accessToken, authentication);
        // In case it was modified
        refreshToken = accessToken.getRefreshToken();
        if (refreshToken != null) {
            tokenStore.storeRefreshToken(refreshToken, authentication);
        }
        return accessToken;

    }

    /**
     * 通过事务 刷新token
     * @param refreshTokenValue
     * @param tokenRequest
     * @return
     * @throws AuthenticationException
     */
    @Override
    @Transactional(noRollbackFor={InvalidTokenException.class, InvalidGrantException.class}, rollbackFor = Exception.class)
    public OAuth2AccessToken refreshAccessToken(String refreshTokenValue, TokenRequest tokenRequest)
            throws AuthenticationException {

        if (!supportRefreshToken) {
            throw new InvalidGrantException("Invalid refresh token: " + refreshTokenValue);
        }

        OAuth2RefreshToken refreshToken = tokenStore.readRefreshToken(refreshTokenValue);
        if (refreshToken == null) {
            throw new InvalidGrantException("Invalid refresh token: " + refreshTokenValue);
        }

        OAuth2Authentication authentication = tokenStore.readAuthenticationForRefreshToken(refreshToken);
        if (this.authenticationManager != null && !authentication.isClientOnly()) {
            // The client has already been authenticated, but the user authentication might be old now, so give it a
            // chance to re-authenticate.
            Authentication user = new PreAuthenticatedAuthenticationToken(authentication.getUserAuthentication(), "", authentication.getAuthorities());
            user = authenticationManager.authenticate(user);
            Object details = authentication.getDetails();
            authentication = new OAuth2Authentication(authentication.getOAuth2Request(), user);
            authentication.setDetails(details);
        }
        String clientId = authentication.getOAuth2Request().getClientId();
        if (clientId == null || !clientId.equals(tokenRequest.getClientId())) {
            throw new InvalidGrantException("Wrong client for this refresh token: " + refreshTokenValue);
        }

        // clear out any access tokens already associated with the refresh
        // token.
        tokenStore.removeAccessTokenUsingRefreshToken(refreshToken);

        if (isExpired(refreshToken)) {
            tokenStore.removeRefreshToken(refreshToken);
            throw new InvalidTokenException("Invalid refresh token (expired): " + refreshToken);
        }

        authentication = createRefreshedAuthentication(authentication, tokenRequest);

        if (!reuseRefreshToken) {
            tokenStore.removeRefreshToken(refreshToken);
            refreshToken = createRefreshToken(authentication);
        }

        OAuth2AccessToken accessToken = createAccessToken(authentication, refreshToken);
        tokenStore.storeAccessToken(accessToken, authentication);
        if (!reuseRefreshToken) {
            tokenStore.storeRefreshToken(accessToken.getRefreshToken(), authentication);
        }
        return accessToken;
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        return tokenStore.getAccessToken(authentication);
    }

    /**
     * 创建一个刷新的身份验证
     *
     * @param authentication 身份验证.
     * @param request 刷新的令牌的范围。
     * @return 刷新的身份验证
     * @throws InvalidScopeException 如果请求的范围是无效的或超过原来的范围.
     */
    private OAuth2Authentication createRefreshedAuthentication(OAuth2Authentication authentication, TokenRequest request) {
        OAuth2Authentication narrowed = authentication;
        Set<String> scope = request.getScope();
        OAuth2Request clientAuth = authentication.getOAuth2Request().refresh(request);
        if (scope != null && !scope.isEmpty()) {
            Set<String> originalScope = clientAuth.getScope();
            if (originalScope == null || !originalScope.containsAll(scope)) {
                throw new InvalidScopeException("Unable to narrow the scope of the client authentication to " + scope
                        + ".", originalScope);
            }
            else {
                clientAuth = clientAuth.narrowScope(scope);
            }
        }
        narrowed = new OAuth2Authentication(clientAuth, authentication.getUserAuthentication());
        return narrowed;
    }

    protected boolean isExpired(OAuth2RefreshToken refreshToken) {
        if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
            ExpiringOAuth2RefreshToken expiringToken = (ExpiringOAuth2RefreshToken) refreshToken;
            return expiringToken.getExpiration() == null
                    || System.currentTimeMillis() > expiringToken.getExpiration().getTime();
        }
        return false;
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        return tokenStore.readAccessToken(accessToken);
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessTokenValue) throws AuthenticationException,
            InvalidTokenException {
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(accessTokenValue);
        if (accessToken == null) {
            throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
        }
        else if (accessToken.isExpired()) {
            tokenStore.removeAccessToken(accessToken);
            throw new InvalidTokenException("Access token expired: " + accessTokenValue);
        }

        OAuth2Authentication result = tokenStore.readAuthentication(accessToken);
        if (result == null) {
            // in case of race condition
            throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
        }
        if (clientDetailsService != null) {
            String clientId = result.getOAuth2Request().getClientId();
            try {
                clientDetailsService.loadClientByClientId(clientId);
            }
            catch (ClientRegistrationException e) {
                throw new InvalidTokenException("Client not valid: " + clientId, e);
            }
        }
        return result;
    }

    public String getClientId(String tokenValue) {
        OAuth2Authentication authentication = tokenStore.readAuthentication(tokenValue);
        if (authentication == null) {
            throw new InvalidTokenException("Invalid access token: " + tokenValue);
        }
        OAuth2Request clientAuth = authentication.getOAuth2Request();
        if (clientAuth == null) {
            throw new InvalidTokenException("Invalid access token (no client id): " + tokenValue);
        }
        return clientAuth.getClientId();
    }

    @Override
    public boolean revokeToken(String tokenValue) {
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
        if (accessToken == null) {
            return false;
        }
        if (accessToken.getRefreshToken() != null) {
            tokenStore.removeRefreshToken(accessToken.getRefreshToken());
        }
        tokenStore.removeAccessToken(accessToken);
        return true;
    }

    private OAuth2RefreshToken createRefreshToken(OAuth2Authentication authentication) {
        if (!isSupportRefreshToken(authentication.getOAuth2Request())) {
            return null;
        }
        int validitySeconds = getRefreshTokenValiditySeconds(authentication.getOAuth2Request());
        String value = UUID.randomUUID().toString();
        if (validitySeconds > 0) {
            return new DefaultExpiringOAuth2RefreshToken(value, new Date(System.currentTimeMillis()
                    + (validitySeconds * 1000L)));
        }
        return new DefaultOAuth2RefreshToken(value);
    }

    private OAuth2AccessToken createAccessToken(OAuth2Authentication authentication, OAuth2RefreshToken refreshToken) {
        DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(UUID.randomUUID().toString());
        int validitySeconds = getAccessTokenValiditySeconds(authentication.getOAuth2Request());
        if (validitySeconds > 0) {
            token.setExpiration(new Date(System.currentTimeMillis() + (validitySeconds * 1000L)));
        }
        token.setRefreshToken(refreshToken);
        token.setScope(authentication.getOAuth2Request().getScope());

        return accessTokenEnhancer != null ? accessTokenEnhancer.enhance(token, authentication) : token;
    }

    /**
     * The access token validity period in seconds
     *
     * @param clientAuth the current authorization request
     * @return the access token validity period in seconds
     */
    protected int getAccessTokenValiditySeconds(OAuth2Request clientAuth) {
        if (clientDetailsService != null) {
            ClientDetails client = clientDetailsService.loadClientByClientId(clientAuth.getClientId());
            Integer validity = client.getAccessTokenValiditySeconds();
            if (validity != null) {
                return validity;
            }
        }
        return accessTokenValiditySeconds;
    }

    /**
     * The refresh token validity period in seconds
     *
     * @param clientAuth the current authorization request
     * @return the refresh token validity period in seconds
     */
    protected int getRefreshTokenValiditySeconds(OAuth2Request clientAuth) {
        if (clientDetailsService != null) {
            ClientDetails client = clientDetailsService.loadClientByClientId(clientAuth.getClientId());
            Integer validity = client.getRefreshTokenValiditySeconds();
            if (validity != null) {
                return validity;
            }
        }
        return refreshTokenValiditySeconds;
    }

    /**
     * Is a refresh token supported for this client (or the global setting if
     * {@link #setClientDetailsService(ClientDetailsService) clientDetailsService} is not set.
     *
     * @param clientAuth the current authorization request
     * @return boolean to indicate if refresh token is supported
     */
    protected boolean isSupportRefreshToken(OAuth2Request clientAuth) {
        if (clientDetailsService != null) {
            ClientDetails client = clientDetailsService.loadClientByClientId(clientAuth.getClientId());
            return client.getAuthorizedGrantTypes().contains("refresh_token");
        }
        return this.supportRefreshToken;
    }

    /**
     * An access token enhancer that will be applied to a new token before it is saved in the token store.
     *
     * @param accessTokenEnhancer the access token enhancer to set
     */
    public void setTokenEnhancer(TokenEnhancer accessTokenEnhancer) {
        this.accessTokenEnhancer = accessTokenEnhancer;
    }

    /**
     * The validity (in seconds) of the refresh token. If less than or equal to zero then the tokens will be
     * non-expiring.
     *
     * @param refreshTokenValiditySeconds The validity (in seconds) of the refresh token.
     */
    public void setRefreshTokenValiditySeconds(int refreshTokenValiditySeconds) {
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

    /**
     * The default validity (in seconds) of the access token. Zero or negative for non-expiring tokens. If a client
     * details service is set the validity period will be read from the client, defaulting to this value if not defined
     * by the client.
     *
     * @param accessTokenValiditySeconds The validity (in seconds) of the access token.
     */
    public void setAccessTokenValiditySeconds(int accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }

    /**
     * Whether to support the refresh token.
     *
     * @param supportRefreshToken Whether to support the refresh token.
     */
    public void setSupportRefreshToken(boolean supportRefreshToken) {
        this.supportRefreshToken = supportRefreshToken;
    }

    /**
     * Whether to reuse refresh tokens (until expired).
     *
     * @param reuseRefreshToken Whether to reuse refresh tokens (until expired).
     */
    public void setReuseRefreshToken(boolean reuseRefreshToken) {
        this.reuseRefreshToken = reuseRefreshToken;
    }

    /**
     * The persistence strategy for token storage.
     *
     * @param tokenStore the store for access and refresh tokens.
     */
    public void setTokenStore(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    /**
     * An authentication manager that will be used (if provided) to check the user authentication when a token is
     * refreshed.
     *
     * @param authenticationManager the authenticationManager to set
     */
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * The client details service to use for looking up clients (if necessary). Optional if the access token expiry is
     * set globally via {@link #setAccessTokenValiditySeconds(int)}.
     *
     * @param clientDetailsService the client details service
     */
    public void setClientDetailsService(ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
    }

}
