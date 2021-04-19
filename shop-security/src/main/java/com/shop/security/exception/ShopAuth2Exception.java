
package com.shop.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

/**
 * @author yzh
 */
public abstract class ShopAuth2Exception extends AuthenticationException {

	public ShopAuth2Exception(String msg) {
		super(msg);
	}

	public int getHttpErrorCode() {
		// 400 not 401
		return HttpStatus.BAD_REQUEST.value();
	}

	public abstract String getOAuth2ErrorCode();
}
