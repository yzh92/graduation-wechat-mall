package com.shop.security.exception;

/**
 * @author yzh
 */
public class UsernameNotFoundExceptionBase extends BaseShopAuth2Exception {

    public UsernameNotFoundExceptionBase(String msg) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "username_not_found";
    }
}
