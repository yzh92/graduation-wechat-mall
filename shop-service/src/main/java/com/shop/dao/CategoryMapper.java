package com.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.bean.model.Category;

import java.util.List;

public interface CategoryMapper extends BaseMapper<Category> {

	List<Category> listByParentId(Long parentId);

	List<Category> tableCategory(Long shopId);

	List<Category> listCategoryAndProdByShopId(Long shopId);
}