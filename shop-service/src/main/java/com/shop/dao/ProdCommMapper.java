package com.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.bean.app.dto.ProdCommDataDto;
import com.shop.bean.app.dto.ProdCommDto;
import com.shop.bean.model.ProdComm;
import org.apache.ibatis.annotations.Param;

public interface ProdCommMapper extends BaseMapper<ProdComm> {
    ProdCommDataDto getProdCommDataByProdId(@Param("prodId") Long prodId, @Param("userId") String userId);

    IPage<ProdCommDto> getProdCommDtoPageByProdId(@Param("page") Page page, @Param("prodId") Long prodId, @Param("evaluate") Integer evaluate, @Param("userId") String userId);

    IPage<ProdCommDto> getProdCommDtoPageByUserId(Page page, @Param("userId") String userId);

    IPage<ProdComm> getProdCommPage(Page page, @Param("prodComm") ProdComm prodComm);
}