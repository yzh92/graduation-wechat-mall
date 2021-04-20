package com.shop.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.bean.app.dto.ProdCommDataDto;
import com.shop.bean.app.dto.ProdCommDto;
import com.shop.bean.model.ProdComm;
import com.shop.common.util.Arith;
import com.shop.dao.ProdCommMapper;
import com.shop.service.ProdCommService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 商品评论
 *
 * @author yzh
 */
@Service
public class ProdCommServiceImpl extends ServiceImpl<ProdCommMapper, ProdComm> implements ProdCommService {

    @Autowired
    private ProdCommMapper prodCommMapper;

    @Override
    public ProdCommDataDto getProdCommDataByProdId(Long prodId, String userId) {
        ProdCommDataDto prodCommDataDto=prodCommMapper.getProdCommDataByProdId(prodId, userId);
        //计算出好评率
        if(prodCommDataDto.getPraiseNumber() == 0||prodCommDataDto.getNumber() == 0){
            prodCommDataDto.setPositiveRating(0.0);
        }else{
            prodCommDataDto.setPositiveRating(Arith.mul(Arith.div(prodCommDataDto.getPraiseNumber(),prodCommDataDto.getNumber()),100));
        }
        return prodCommDataDto;
    }

    @Override
    public IPage<ProdCommDto> getProdCommDtoPageByUserId(Page page, String userId) {
        return prodCommMapper.getProdCommDtoPageByUserId(page,userId);
    }

    @Override
    public IPage<ProdCommDto> getProdCommDtoPageByProdId(Page page, Long prodId, Integer evaluate, String userId) {

        IPage<ProdCommDto> prodCommDtos = prodCommMapper.getProdCommDtoPageByProdId(page, prodId, evaluate, userId);
        prodCommDtos.getRecords().forEach(prodCommDto -> {
            // 匿名评价
            if (prodCommDto.getIsAnonymous() == 1) {
                prodCommDto.setNickName(null);
            }
        });
        return prodCommDtos;
    }

    @Override
    public IPage<ProdComm> getProdCommPage(Page page,ProdComm prodComm) {
        return prodCommMapper.getProdCommPage(page,prodComm);
    }
}
