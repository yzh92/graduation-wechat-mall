package com.shop.api.security;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.emoji.EmojiUtil;
import com.shop.security.service.ShopUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * token 增强 加入另外的返回值
 * @author yzh
 */
@Component
public class ApiTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String,Object> additionalInfo = new HashMap<>(8);
        ShopUser shopUser = (ShopUser) authentication.getUserAuthentication().getPrincipal();
        additionalInfo.put("userId",shopUser.getUserId());
        additionalInfo.put("nickName", EmojiUtil.toUnicode(StrUtil.isBlank(shopUser.getName())? "" : shopUser.getName()));
        additionalInfo.put("pic",shopUser.getPic());
        additionalInfo.put("enabled",shopUser.isEnabled());
        //将另外的返回值加入 token返回
        ((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
