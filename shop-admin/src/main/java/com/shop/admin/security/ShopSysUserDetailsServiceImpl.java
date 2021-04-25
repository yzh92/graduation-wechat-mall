package com.shop.admin.security;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.shop.common.util.CacheManagerUtil;
import com.shop.security.enums.App;
import com.shop.security.exception.UsernameNotFoundExceptionBase;
import com.shop.security.model.AppConnect;
import com.shop.security.service.ShopSysUser;
import com.shop.security.service.ShopUser;
import com.shop.security.service.ShopUserDetailsService;
import com.shop.sys.constant.Constant;
import com.shop.sys.dao.SysMenuMapper;
import com.shop.sys.dao.SysUserMapper;
import com.shop.sys.model.SysMenu;
import com.shop.sys.model.SysUser;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户详细信息
 *
 * @author
 */
@Slf4j
@Service
@AllArgsConstructor
public class ShopSysUserDetailsServiceImpl implements ShopUserDetailsService {
	private final SysMenuMapper sysMenuMapper;
	private final SysUserMapper sysUserMapper;
	private final CacheManagerUtil cacheManagerUtil;

	/**
	 * 用户密码登录
	 *
	 * @param username 用户名
	 * @return
	 * @throws UsernameNotFoundExceptionBase
	 */
	@Override
	@SneakyThrows
	public ShopSysUser loadUserByUsername(String username) {
		return getUserDetails(username);
	}


	/**
	 * 构建userdetails
	 *
	 * @param username 用户名称
	 * @return
	 */
	private ShopSysUser getUserDetails(String username) {
		SysUser sysUser = sysUserMapper.selectByUsername(username);

		if (sysUser == null) {
			throw new UsernameNotFoundExceptionBase("用户不存在");
		}

		Collection<? extends GrantedAuthority> authorities
				= AuthorityUtils.createAuthorityList(getUserPermissions(sysUser.getUserId()).toArray(new String[0]));
		// 构造security用户
		return new ShopSysUser(sysUser.getUserId(), sysUser.getShopId(), sysUser.getUsername(), sysUser.getPassword(), sysUser.getStatus() == 1,
				true, true, true , authorities);
	}

	private Set<String> getUserPermissions(Long userId) {
		List<String> permsList;

		//系统管理员，拥有最高权限
		if(userId == Constant.SUPER_ADMIN_ID){
			List<SysMenu> menuList = sysMenuMapper.selectList(Wrappers.emptyWrapper());


			permsList = menuList.stream().map(SysMenu::getPerms).collect(Collectors.toList());
		}else{
			permsList = sysUserMapper.queryAllPerms(userId);
		}


		Set<String> permsSet = permsList.stream().flatMap((perms)->{
					if (StrUtil.isBlank(perms)) {
						return null;
					}
					return Arrays.stream(perms.trim().split(","));
				}
		).collect(Collectors.toSet());
		return permsSet;
	}

	@Override
	public ShopUser loadUserByAppIdAndBizUserId(App app, String bizUserId) {
		return null;
	}

	@Override
	public void insertUserIfNecessary(AppConnect appConnect) {

	}

	@Override
	public ShopUser loadUserByUserMail(String userMail, String loginPassword) {
		return null;
	}
}
