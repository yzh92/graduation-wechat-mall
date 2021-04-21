package com.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.bean.model.OrderItem;

import java.util.List;

/**
 *
 * @author yzh.
 */
public interface OrderItemService extends IService<OrderItem> {

	List<OrderItem> getOrderItemsByOrderNumber(String orderNumber);

}
