package com.shop.api.security;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.shop.common.util.Json;
import com.shop.security.enums.App;
import com.shop.security.exception.UsernameNotFoundExceptionBase;
import com.shop.security.exception.WxErrorExceptionBase;
import com.shop.security.model.AppConnect;
import com.shop.security.service.ShopUser;
import com.shop.security.service.ShopUserDetailsService;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 *  小程序登陆：此时principal为code
 *      post:http://127.0.0.1:8086/login
 *      {principal:code}
 * @author yzh
 */
@Component
public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ShopUserDetailsService shopUserDetailsService;

    private final WxMaService wxMaService;

    @Autowired
    public LoginAuthenticationFilter(ShopUserDetailsService shopUserDetailsService, WxMaService wxMaService) {
        super("/login");
        this.shopUserDetailsService = shopUserDetailsService;
        this.wxMaService = wxMaService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!ServletUtil.METHOD_POST.equals(request.getMethod())) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        String requestBody = getStringFromStream(request);

        if (StrUtil.isBlank(requestBody)) {
            throw new AuthenticationServiceException("无法获取输入信息");
        }
        MiniAppAuthenticationToken authentication  =  Json.parseObject(requestBody, MiniAppAuthenticationToken.class);
        String code = String.valueOf(authentication.getPrincipal());
        ShopUser loadedUser = null;

        WxMaJscode2SessionResult session = null;

        AppConnect appConnect = new AppConnect();
        appConnect.setAppId(App.MINI.value());
        try {

            session = wxMaService.getUserService().getSessionInfo(code);

            loadedUser = shopUserDetailsService.loadUserByAppIdAndBizUserId(App.MINI,session.getOpenid());
        } catch (WxErrorException e) {
            throw new WxErrorExceptionBase(e.getMessage());
        } catch (UsernameNotFoundExceptionBase var6) {
            if (session == null) {
                throw new WxErrorExceptionBase("无法获取用户登陆信息");
            }
            appConnect.setBizUserId(session.getOpenid());
            appConnect.setBizUnionid(session.getUnionid());
            shopUserDetailsService.insertUserIfNecessary(appConnect);
        }

        if (loadedUser == null) {
            loadedUser = shopUserDetailsService.loadUserByAppIdAndBizUserId(App.MINI, appConnect.getBizUserId());
        }
        MiniAppAuthenticationToken result = new MiniAppAuthenticationToken(loadedUser, authentication.getCredentials());
        result.setDetails(authentication.getDetails());
        return result;
    }

    /**
     * 从请求流中获取字符串
     * @param req
     * @return
     */
    private String getStringFromStream(HttpServletRequest req) {
        ServletInputStream is;
        try {
            is = req.getInputStream();
            int nRead = 1;
            int nTotalRead = 0;
            byte[] bytes = new byte[10240];
            while (nRead > 0) {
                nRead = is.read(bytes, nTotalRead, bytes.length - nTotalRead);
                if (nRead > 0) {
                    nTotalRead = nTotalRead + nRead;
                }
            }
            return new String(bytes, 0, nTotalRead, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    @Autowired
    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
        super.setAuthenticationSuccessHandler(successHandler);
    }

    @Override
    @Autowired
    public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
        super.setAuthenticationFailureHandler(failureHandler);
    }
}