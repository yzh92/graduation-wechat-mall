package com.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.bean.model.OrderItem;
import com.shop.dao.OrderItemMapper;
import com.shop.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author lgh on 2018/09/15.
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

    @Autowired
    private OrderItemMapper orderItemMapper;

	@Override
	@Cacheable(cacheNames = "OrderItems", key = "#orderNumber")
	public List<OrderItem> getOrderItemsByOrderNumber(String orderNumber) {
		return orderItemMapper.listByOrderNumber(orderNumber);
	}


}
