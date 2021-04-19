package com.shop.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

/**
 * @author yzh
 */
public abstract class BaseShopAuth2Exception extends AuthenticationException {

	public BaseShopAuth2Exception(String msg) {
		super(msg);
	}

	public int getHttpErrorCode() {
		// 400 not 401
		return HttpStatus.BAD_REQUEST.value();
	}

	public abstract String getOAuth2ErrorCode();
}
