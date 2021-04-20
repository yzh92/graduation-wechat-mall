package com.shop.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.bean.model.ProdTag;

import java.util.List;

/**
 * 商品分组标签
 *
 * @author yzh
 */
public interface ProdTagService extends IService<ProdTag> {

    List<ProdTag> listProdTag();

    void removeProdTag();
}
