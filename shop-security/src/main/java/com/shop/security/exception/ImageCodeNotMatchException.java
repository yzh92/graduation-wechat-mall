
package com.shop.security.exception;

/**
 * @author yzh
 */
public class ImageCodeNotMatchException extends ShopAuth2Exception {

    public ImageCodeNotMatchException(String msg) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "image_code_not_match";
    }
}
