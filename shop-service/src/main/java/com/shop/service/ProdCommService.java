package com.shop.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.bean.app.dto.ProdCommDataDto;
import com.shop.bean.app.dto.ProdCommDto;
import com.shop.bean.model.ProdComm;


/**
 * 商品评论
 *
 * @author yzh
 */
public interface ProdCommService extends IService<ProdComm> {
    ProdCommDataDto getProdCommDataByProdId(Long prodId, String userId);

    IPage<ProdCommDto> getProdCommDtoPageByUserId(Page page,String userId);

    IPage<ProdCommDto> getProdCommDtoPageByProdId(Page page, Long prodId, Integer evaluate, String userId);

    IPage<ProdComm> getProdCommPage(Page page,ProdComm prodComm);

}
