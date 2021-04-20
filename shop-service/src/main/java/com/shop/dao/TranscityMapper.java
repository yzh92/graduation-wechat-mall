package com.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.bean.model.Transcity;
import com.shop.bean.model.TranscityFree;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TranscityMapper extends BaseMapper<Transcity> {

	void insertTranscities(@Param("transcities") List<Transcity> transcities);

	void insertTranscityFrees(@Param("transcityFrees") List<TranscityFree> transcityFrees);

	List<Transcity> listTranscityAndArea(@Param("transfeeId") Long transfeeId);

	void deleteBatchByTransfeeIds(@Param("transfeeIds") List<Long> transfeeIds);

	void deleteBatchByTransfeeFreeIds(@Param("transfeeFreeIds") List<Long> transfeeFreeIds);

}