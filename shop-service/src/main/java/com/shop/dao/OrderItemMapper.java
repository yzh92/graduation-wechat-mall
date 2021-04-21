package com.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.bean.model.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemMapper extends BaseMapper<OrderItem> {

	List<OrderItem> listByOrderNumber(@Param("orderNumber") String orderNumber);
	
	void insertBatch(List<OrderItem> orderItems);
	
//	List<OrderItem> getPayByOrderNumber(@Param("orderNumber") String orderNumber);

}