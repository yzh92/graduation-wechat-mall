package com.shop.security.exception;

/**
 * @author yzh
 */
public class UsernameNotFoundException extends ShopAuth2Exception {

    public UsernameNotFoundException(String msg) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "username_not_found";
    }
}
