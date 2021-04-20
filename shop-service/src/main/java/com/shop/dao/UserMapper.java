
package com.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.bean.model.User;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author yzh
 */
public interface UserMapper extends BaseMapper<User> {
	/**
	 * 根据appid 与 userid查询用户
	 * @param appId 第三方系统 id 1：微信小程序
	 * @param bizUserId 第三方系统userid
	 * @return User
	 */
	User getUserByBizUserId(@Param("appId")Integer appId, @Param("bizUserId")String bizUserId);

	/**
	 *  根据用户邮箱查询用户
	 * @param userMail
	 * @return User
	 */
	User getUserByUserMail(@Param("userMail") String userMail);
}
