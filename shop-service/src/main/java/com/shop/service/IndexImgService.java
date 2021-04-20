package com.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.bean.model.IndexImg;

import java.util.List;

/**
 * @author yzh
 * Date:2021/4/20
 */
public interface IndexImgService extends IService<IndexImg> {

    void deleteIndexImgsByIds(Long[] ids);

    List<IndexImg> listIndexImgs();

    void removeIndexImgCache();

}
