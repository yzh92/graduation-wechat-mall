package com.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.bean.dto.HotSearchDto;
import com.shop.bean.model.HotSearch;

import java.util.List;

/**
 *
 * @author yzh.
 */
public interface HotSearchService extends IService<HotSearch> {

    List<HotSearchDto> getHotSearchDtoByshopId(Long shopId);

    void removeHotSearchDtoCacheByshopId(Long shopId);
}
