package com.shop.admin.security;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.shop.common.util.Json;
import com.shop.common.util.RedisUtil;
import com.shop.security.constants.SecurityConstants;
import com.shop.security.exception.BadCredentialsExceptionBase;
import com.shop.security.exception.ImageCodeNotMatchExceptionBase;
import com.shop.security.exception.UsernameNotFoundExceptionBase;
import com.shop.security.provider.AuthenticationTokenParser;
import com.shop.security.service.ShopUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 管理员登陆：
 *       post: http://127.0.0.1:8085/login
 *       {principal:username,credentials:password}
 * @author yzh
 */
@Component
public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private ShopUserDetailsService ShopUserDetailsService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public LoginAuthenticationFilter(ShopUserDetailsService ShopUserDetailsService, PasswordEncoder passwordEncoder) {
        super("/login");
        this.ShopUserDetailsService = ShopUserDetailsService;
        this.passwordEncoder = passwordEncoder;
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
        AdminAuthenticationToken adminAuthenticationToken  =  Json.parseObject(requestBody, AdminAuthenticationToken.class);


        String username = adminAuthenticationToken.getPrincipal() == null?"NONE_PROVIDED":adminAuthenticationToken.getName();


        String kaptchaKey = SecurityConstants.SPRING_SECURITY_RESTFUL_IMAGE_CODE + adminAuthenticationToken.getSessionUUID();

        String kaptcha = RedisUtil.get(kaptchaKey);

        RedisUtil.del(kaptchaKey);

        if(StrUtil.isBlank(adminAuthenticationToken.getImageCode()) || !adminAuthenticationToken.getImageCode().equalsIgnoreCase(kaptcha)){
            throw new ImageCodeNotMatchExceptionBase("验证码有误");
        }

        UserDetails user;
        try {
            user = ShopUserDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundExceptionBase var6) {
            throw new UsernameNotFoundExceptionBase("账号或密码不正确");
        }

        String encodedPassword = user.getPassword();
        String rawPassword = adminAuthenticationToken.getCredentials().toString();

        // 密码不正确
        if (!passwordEncoder.matches(rawPassword,encodedPassword)){
            throw new BadCredentialsExceptionBase("账号或密码不正确");
        }

        if (!user.isEnabled()) {
            throw new UsernameNotFoundExceptionBase("账号已被锁定,请联系管理员");
        }
        AdminAuthenticationToken result = new AdminAuthenticationToken(user, adminAuthenticationToken.getCredentials());
        result.setDetails(adminAuthenticationToken.getDetails());
        return result;
    }


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
