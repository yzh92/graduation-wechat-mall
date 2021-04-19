package com.shop.security.exception;

/**
 * 密码不正确
 * @author yzh
 */
public class WxErrorExceptionBase extends BaseShopAuth2Exception {
    public WxErrorExceptionBase(String msg) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "wx_error_exception";
    }
}
