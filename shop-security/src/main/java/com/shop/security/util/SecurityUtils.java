package com.shop.security.util;


import com.shop.security.exception.UnauthorizedExceptionBase;
import com.shop.security.service.ShopSysUser;
import com.shop.security.service.ShopUser;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 安全工具类
 *
 * @author L.cm
 */
@UtilityClass
public class SecurityUtils {
	/**
	 * 获取Authentication
	 */
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 获取用户
	 */
	public ShopUser getUser() {
		Authentication authentication = getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal instanceof ShopUser) {
			return (ShopUser) principal;
		}
		throw new UnauthorizedExceptionBase("无法获取普通用户信息");
	}

	/**
	 * 获取系统用户
	 */
	public ShopSysUser getSysUser() {
		Authentication authentication = getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal instanceof ShopSysUser) {
			return (ShopSysUser) principal;
		}
		throw new UnauthorizedExceptionBase("无法获取系统用户信息");
	}
}
