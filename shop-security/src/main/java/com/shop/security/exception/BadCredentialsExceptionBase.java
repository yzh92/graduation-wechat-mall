package com.shop.security.exception;

/**
 * 密码不正确
 * @author yzh
 */
public class BadCredentialsExceptionBase extends BaseShopAuth2Exception {
    public BadCredentialsExceptionBase(String msg) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "bad_credentials";
    }
}
