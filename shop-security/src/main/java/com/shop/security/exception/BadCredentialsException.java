package com.shop.security.exception;

/**
 * 密码不正确
 * @author yzh
 */
public class BadCredentialsException extends ShopAuth2Exception{
    public BadCredentialsException(String msg) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "bad_credentials";
    }
}
