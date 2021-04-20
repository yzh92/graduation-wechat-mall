package com.shop.dao;

import com.shop.bean.dto.HotSearchDto;
import com.shop.bean.model.HotSearch;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author yzh
 */
public interface HotSearchMapper extends BaseMapper<HotSearch> {
    List<HotSearchDto> getHotSearchDtoByShopId(Long shopId);
}