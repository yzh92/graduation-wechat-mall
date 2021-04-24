package com.shop.bean.event;

import com.shop.bean.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *  取消订单的事件
 * @author yzh
 */
@Data
@AllArgsConstructor
public class CancelOrderEvent {

    private Order order;
}
