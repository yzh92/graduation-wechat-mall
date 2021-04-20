package com.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.bean.model.Transfee;
import com.shop.bean.model.TransfeeFree;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TransfeeMapper extends BaseMapper<Transfee> {

	void insertTransfees(List<Transfee> transfees);

	void insertTransfeeFrees(List<TransfeeFree> transfeeFrees);

	void deleteByTransportId(@Param("transportId") Long transportId);

	void deleteTransfeeFreesByTransportId(@Param("transportId") Long transportId);


}