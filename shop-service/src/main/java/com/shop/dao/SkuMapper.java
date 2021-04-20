package com.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.bean.model.Sku;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author yzh
 */
public interface SkuMapper extends BaseMapper<Sku> {

	/**
	 * 批量插入sku
	 * @param prodId 商品id
	 * @param skuList sku列表
	 */
	void insertBatch(@Param("prodId") Long prodId, @Param("skuList") List<Sku> skuList);

	List<Sku> listByProdId(Long prodId);

	int updateStocks(@Param("sku") Sku sku);
	
	void deleteByProdId(@Param("prodId") Long prodId);

	void returnStock(@Param("skuCollect") Map<Long, Integer> skuCollect);

}