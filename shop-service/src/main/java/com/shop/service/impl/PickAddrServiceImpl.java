package com.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.bean.model.PickAddr;
import com.shop.dao.PickAddrMapper;
import com.shop.service.PickAddrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author yzh
 */
@Service
public class PickAddrServiceImpl extends ServiceImpl<PickAddrMapper, PickAddr> implements PickAddrService {

    @Autowired
    private PickAddrMapper pickAddrMapper;

	@Override
	public void deleteByIds(Long[] ids) {
		pickAddrMapper.deleteByIds(ids);
	}


}
