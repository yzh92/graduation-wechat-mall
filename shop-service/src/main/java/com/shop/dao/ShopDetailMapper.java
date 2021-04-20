
package com.shop.dao;

import com.shop.bean.model.ShopDetail;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface ShopDetailMapper extends BaseMapper<ShopDetail> {

    Integer getIsDistributionByShopId(Long shopId);
}