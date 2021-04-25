package com.shop.security.exception;

/**
 * 验证码
 * @author yzh
 */
public class ImageCodeNotMatchExceptionBase extends BaseShopAuth2Exception {

    public ImageCodeNotMatchExceptionBase(String msg) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "image_code_not_match";
    }
}
