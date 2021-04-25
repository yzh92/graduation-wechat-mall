package com.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.bean.model.PickAddr;

/**
 *
 * @author yzh
 */
public interface PickAddrService extends IService<PickAddr> {

	void deleteByIds(Long[] ids);

}
