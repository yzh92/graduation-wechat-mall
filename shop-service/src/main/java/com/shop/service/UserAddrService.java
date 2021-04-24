package com.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.bean.model.UserAddr;

/**
 * @author yzh
 */
public interface UserAddrService extends IService<UserAddr> {

	UserAddr getDefaultUserAddr(String userId);

	/**
	 * 更新默认地址
	 * @param addrId 默认地址id
	 * @param userId 用户id
	 */
	void updateDefaultUserAddr(Long addrId, String userId);


    void removeUserAddrByUserId(Long addrId, String userId);

    UserAddr getUserAddrByUserId(Long addrId, String userId);
}

