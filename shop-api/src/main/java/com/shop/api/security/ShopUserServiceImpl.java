package com.shop.api.security;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.emoji.EmojiUtil;
import com.shop.security.service.ShopUser;
import com.shop.security.service.ShopUserDetailsService;
import com.shop.bean.model.User;
import com.shop.common.annotation.RedisLock;
import com.shop.common.exception.ShopBindException;
import com.shop.dao.UserMapper;
import com.shop.security.dao.AppConnectMapper;
import com.shop.security.enums.App;
import com.shop.security.exception.UsernameNotFoundExceptionBase;
import com.shop.security.model.AppConnect;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 用户详细信息
 *
 * @author
 */
@Slf4j
@Service
@AllArgsConstructor
public class ShopUserServiceImpl implements ShopUserDetailsService {

	private final UserMapper userMapper;

	private final AppConnectMapper appConnectMapper;

	private final PasswordEncoder passwordEncoder;
	@Override
	@SneakyThrows
	public ShopUser loadUserByUsername(String username) {
		if (StrUtil.isBlank(username) || !username.contains(StrUtil.COLON) ) {
			throw new UsernameNotFoundExceptionBase("无法获取用户信息");
		}
		String[] splitInfo = username.split(StrUtil.COLON);
		App app = App.instance(Integer.valueOf(splitInfo[0]));
		String bizUserId = splitInfo[1];
		return loadUserByAppIdAndBizUserId(app,bizUserId);
	}

	/**
	 * 获取前端登陆的用户信息
	 *
	 * @param app
	 * @param bizUserId openId
	 * @return UserDetails
	 * @throws UsernameNotFoundExceptionBase
	 */
	@Override
	public ShopUser loadUserByAppIdAndBizUserId(App app, String bizUserId) {

		String cacheKey = app.value() + StrUtil.COLON + bizUserId;

		User user = userMapper.getUserByBizUserId(app.value(), bizUserId);
		if (user == null) {
			throw new UsernameNotFoundExceptionBase("无法获取用户信息");
		}
		String name = StrUtil.isBlank(user.getRealName()) ? user.getNickName() : user.getRealName();
		ShopUser shopUser = new ShopUser(user.getUserId(), bizUserId, app.value(), user.getStatus() == 1);
		shopUser.setName(name);
		shopUser.setPic(user.getPic());

		return shopUser;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@RedisLock(lockName = "insertUser", key = "#appConnect.appId + ':' + #appConnect.bizUserId")
	@Caching(evict = {
			@CacheEvict(cacheNames = "shop_user", key = "#appConnect.appId + ':' + #appConnect.bizUserId"),
			@CacheEvict(cacheNames = "AppConnect", key = "#appConnect.appId + ':' + #appConnect.bizUserId")
	})
	public void insertUserIfNecessary(AppConnect appConnect) {
		// 进入锁后再重新判断一遍用户是否创建
		AppConnect dbAppConnect = appConnectMapper.getByBizUserId(appConnect.getBizUserId(), appConnect.getAppId());
		if(dbAppConnect != null) {
			return;
		}

		String bizUnionId = appConnect.getBizUnionid();
		String userId = null;
		User user;

		if (StrUtil.isNotBlank(bizUnionId)) {
			userId = appConnectMapper.getUserIdByUnionId(bizUnionId);
		}
		if (StrUtil.isBlank(userId)) {
			userId = IdUtil.simpleUUID();
			Date now = new Date();
			user = new User();
			user.setUserId(userId);
			user.setModifyTime(now);
			user.setUserRegtime(now);
			user.setStatus(1);
			user.setNickName(EmojiUtil.toAlias(StrUtil.isBlank(appConnect.getNickName()) ? "" : appConnect.getNickName()));
			user.setPic(appConnect.getImageUrl());
			userMapper.insert(user);
		} else {
			user = userMapper.selectById(userId);
		}

		appConnect.setUserId(user.getUserId());

		appConnectMapper.insert(appConnect);
	}

	@Override
	public ShopUser loadUserByUserMail(String userMail, String loginPassword) {
		User user = userMapper.getUserByUserMail(userMail);
		if (user == null) {
			throw new ShopBindException("用户不存在");
		}
		String paramPassword = passwordEncoder.encode(loginPassword);
		if (!passwordEncoder.matches(loginPassword, user.getLoginPassword())) {
			// 原密码不正确
			throw new ShopBindException("密码不正确");
		}
		String name = StrUtil.isBlank(user.getRealName()) ? user.getNickName() : user.getRealName();
		ShopUser shopUser = new ShopUser(user.getUserId(), loginPassword, user.getStatus() == 1);
		shopUser.setName(name);
		shopUser.setPic(user.getPic());
		return shopUser;
	}
}
