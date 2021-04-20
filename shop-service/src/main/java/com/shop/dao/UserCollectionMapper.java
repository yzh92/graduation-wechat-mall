package com.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.bean.app.dto.UserCollectionDto;
import com.shop.bean.model.UserCollection;

/**
 * 用户收藏表
 *
 * @author yzh
 */
public interface UserCollectionMapper extends BaseMapper<UserCollection> {
   IPage<UserCollectionDto> getUserCollectionDtoPageByUserId(Page page, String userId);

}
