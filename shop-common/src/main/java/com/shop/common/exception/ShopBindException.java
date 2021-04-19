package com.shop.common.exception;

import com.shop.common.enums.ShopHttpStatus;
import org.springframework.http.HttpStatus;

public class ShopBindException extends RuntimeException{

	/**
	 *
	 */
	private static final long serialVersionUID = -4137688758944857209L;

	/**
	 * http状态码
	 */
	private Integer httpStatusCode;


	/**
	 * @param httpStatus http状态码
	 */
	public ShopBindException(ShopHttpStatus httpStatus) {
		super(httpStatus.getMsg());
		this.httpStatusCode = httpStatus.value();
	}

	/**
	 * @param httpStatus http状态码
	 */
	public ShopBindException(ShopHttpStatus httpStatus, String msg) {
		super(msg);
		this.httpStatusCode = httpStatus.value();
	}


	public ShopBindException(String msg) {
		super(msg);
		this.httpStatusCode = HttpStatus.BAD_REQUEST.value();
	}


	public Integer getHttpStatusCode() {
		return httpStatusCode;
	}

}
