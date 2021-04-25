package com.shop.admin.security;

import com.shop.security.service.ShopSysUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * token增强
 * @author yzh
 */
@Component
public class AdminTokenEnhancer implements TokenEnhancer {


    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        final Map<String, Object> additionalInfo = new HashMap<>(8);
        ShopSysUser ShopSysUser = (ShopSysUser) authentication.getUserAuthentication().getPrincipal();
        additionalInfo.put("shopId", ShopSysUser.getShopId());
        additionalInfo.put("userId", ShopSysUser.getUserId());
        additionalInfo.put("authorities", ShopSysUser.getAuthorities());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
