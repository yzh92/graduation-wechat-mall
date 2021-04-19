package com.shop.security.exception;

/**
 * 密码不正确
 */
public class WxErrorException extends ShopAuth2Exception{
    public WxErrorException(String msg) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "wx_error_exception";
    }
}
