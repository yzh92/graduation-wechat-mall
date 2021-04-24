package com.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.bean.model.OrderSettlement;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderSettlementMapper extends BaseMapper<OrderSettlement> {

	void updateByOrderNumberAndUserId(@Param("orderSettlement") OrderSettlement orderSettlement);

	List<OrderSettlement> getSettlementsByPayNo(@Param("payNo") String payNo);

	void updateSettlementsByPayNo(@Param("outTradeNo") String outTradeNo, @Param("transactionId") String transactionId);

	int updateToPay(@Param("payNo") String payNo, @Param("version") Integer version);
}