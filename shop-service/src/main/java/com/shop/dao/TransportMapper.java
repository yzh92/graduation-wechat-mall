package com.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.bean.model.TransfeeFree;
import com.shop.bean.model.Transport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TransportMapper extends BaseMapper<Transport> {

	Transport getTransportAndTransfeeAndTranscity(@Param("id") Long id);

	void deleteTransports(@Param("ids") Long[] ids);

	List<TransfeeFree> getTransfeeFreeAndTranscityFreeByTransportId(@Param("transportId") Long transportId);

}