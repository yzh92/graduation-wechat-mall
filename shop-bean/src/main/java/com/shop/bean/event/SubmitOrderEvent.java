package com.shop.bean.event;

import com.shop.bean.app.dto.ShopCartOrderMergerDto;
import com.shop.bean.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 提交订单时的事件
 * @author yzh
 */
@Data
@AllArgsConstructor
public class SubmitOrderEvent {
    /**
     * 完整的订单信息
     */
    private final ShopCartOrderMergerDto mergerOrder;

    private List<Order> orders;

}
