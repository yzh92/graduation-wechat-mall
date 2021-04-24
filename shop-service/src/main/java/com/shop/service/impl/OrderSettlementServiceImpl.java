package com.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.bean.model.OrderSettlement;
import com.shop.dao.OrderSettlementMapper;
import com.shop.service.OrderSettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author yzh
 */
@Service
public class OrderSettlementServiceImpl extends ServiceImpl<OrderSettlementMapper, OrderSettlement> implements OrderSettlementService {

    @Autowired
    private OrderSettlementMapper orderSettlementMapper;

	@Override
	public void updateSettlementsByPayNo(String outTradeNo, String transactionId) {
		orderSettlementMapper.updateSettlementsByPayNo(outTradeNo, transactionId);
	}

}
