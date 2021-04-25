package com.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.bean.model.ProdPropValue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品属性值Mapper
 * @author yzh
 */
public interface ProdPropValueMapper extends BaseMapper<ProdPropValue> {

	void insertPropValues(@Param("propId") Long propId, @Param("prodPropValues") List<ProdPropValue> prodPropValues);

	void deleteByPropId(@Param("propId") Long propId);
}