/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.shop.security.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.security.model.AppConnect;
import com.shop.security.enums.App;

/**
 *
 * @author yzh
 */

public interface AppConnectService extends IService<AppConnect> {
	/**
	 * 通过openid 查询用户是否存在
	 *
	 * @param bizUserId openid
	 * @param app
	 * @return Result<XxxxDO>
	 */
	AppConnect getByBizUserId(String bizUserId, App app);
}
