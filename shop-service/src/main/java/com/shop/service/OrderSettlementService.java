package com.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.bean.model.OrderSettlement;

/**
 *
 * @author yzh
 */
public interface OrderSettlementService extends IService<OrderSettlement> {

	/**
	 * 根据内部订单号更新order settlement
	 */
	void updateSettlementsByPayNo(String outTradeNo, String transactionId);
}
