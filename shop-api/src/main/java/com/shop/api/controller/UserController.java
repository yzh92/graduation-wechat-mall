package com.shop.api.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.emoji.EmojiUtil;
import com.shop.bean.app.dto.UserDto;
import com.shop.bean.app.param.UserInfoParam;
import com.shop.bean.model.User;
import com.shop.common.util.CacheManagerUtil;
import com.shop.security.enums.App;
import com.shop.security.util.SecurityUtils;
import com.shop.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yzh
 */
@RestController
@RequestMapping("/p/user")
@Api(tags="用户接口")
@AllArgsConstructor
public class UserController {

	private final UserService userService;

	private final MapperFacade mapperFacade;

	private final CacheManagerUtil cacheManagerUtil;

	private final ConsumerTokenServices consumerTokenServices;
	/**
	 * 查看用户接口
	 */
	@GetMapping("/userInfo")
	@ApiOperation(value="查看用户信息", notes="根据用户ID（userId）获取用户信息")
	public ResponseEntity<UserDto> userInfo() {
		String userId = SecurityUtils.getUser().getUserId();
		User user = userService.getById(userId);
		UserDto userDto = mapperFacade.map(user, UserDto.class);
		return ResponseEntity.ok(userDto);
	}

	@PutMapping("/setUserInfo")
	@ApiOperation(value="设置用户信息", notes="设置用户信息")
	public ResponseEntity<Void> setUserInfo(@RequestBody UserInfoParam userInfoParam) {
		String userId = SecurityUtils.getUser().getUserId();
		User user = new User();
		user.setUserId(userId);
		user.setPic(userInfoParam.getAvatarUrl());
		user.setNickName(EmojiUtil.toAlias(userInfoParam.getNickName()));
		userService.updateById(user);
		String cacheKey = App.MINI.value() + StrUtil.COLON + SecurityUtils.getUser().getBizUserId();
		cacheManagerUtil.evictCache("yami_user", cacheKey);
		return ResponseEntity.ok(null);
	}

	/**
	 * 退出登录,并清除redis中的token
	 **/
	@GetMapping("/logout")
	public Boolean removeToken(HttpServletRequest httpRequest){
		String authorization = httpRequest.getHeader("authorization");
		String token = authorization.replace("bearer", "");
		return consumerTokenServices.revokeToken(token);
	}
}
