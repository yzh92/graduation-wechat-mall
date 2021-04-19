package com.shop.security.provider;



import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.emoji.EmojiUtil;
import com.shop.security.enums.App;
import com.shop.security.exception.UsernameNotFoundExceptionBase;
import com.shop.security.exception.WxErrorExceptionBase;
import com.shop.security.model.AppConnect;
import com.shop.security.service.ShopUser;
import com.shop.security.service.ShopUserDetailsService;
import com.shop.security.token.MpAuthenticationToken;
import com.shop.security.token.MyAuthenticationToken;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 微信公众号登陆
 * @author yzh
 */
//@Component
@AllArgsConstructor
public class MpAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final ShopUserDetailsService ShopUserDetailsService;

    private final WxMpService wxMpService;



    @Override
    protected Authentication createSuccessAuthentication(Authentication authentication, UserDetails user) {
        MpAuthenticationToken result = new MpAuthenticationToken(user, authentication.getCredentials());
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    protected UserDetails retrieveUser(String code, Authentication authentication) throws AuthenticationException {
        ShopUser loadedUser = null;
        // 如果使用debugger 模式，则返回debugger的用户
        if (BooleanUtil.isTrue(((MyAuthenticationToken)authentication).getDebugger())) {
            loadedUser = new ShopUser("1" , "debuggerOpenId" ,  this.getAppInfo().value(), true);
            loadedUser.setDebugger(true);
            return loadedUser;
        }

        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;

        AppConnect appConnect = new AppConnect();
        appConnect.setAppId(this.getAppInfo().value());

        try {

            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            loadedUser = ShopUserDetailsService.loadUserByAppIdAndBizUserId(this.getAppInfo(),wxMpOAuth2AccessToken.getOpenId());

        } catch (WxErrorException e) {
            throw new WxErrorExceptionBase(e.getMessage());
        } catch (UsernameNotFoundExceptionBase var6) {
            WxMpUser wxMpUser = null;
            try {
                wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
            } catch (WxErrorException e) {
                throw new WxErrorExceptionBase(e.getMessage());
            }
            appConnect.setNickName(EmojiUtil.toAlias(StrUtil.isBlank(wxMpUser.getNickname())? "": wxMpUser.getNickname()));
            appConnect.setImageUrl(wxMpUser.getHeadImgUrl());
            appConnect.setBizUserId(wxMpUser.getOpenId());
            appConnect.setBizUnionid(wxMpUser.getUnionId());
            ShopUserDetailsService.insertUserIfNecessary(appConnect);

        }

        if (loadedUser == null) {
            loadedUser = ShopUserDetailsService.loadUserByAppIdAndBizUserId(this.getAppInfo(), appConnect.getBizUserId());
        }
        return loadedUser;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MpAuthenticationToken.class.isAssignableFrom(authentication);
    }


    @Override
    protected App getAppInfo() {
        return App.MP;
    }
}
