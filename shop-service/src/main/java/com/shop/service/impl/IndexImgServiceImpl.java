package com.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.bean.model.IndexImg;
import com.shop.dao.IndexImgMapper;
import com.shop.service.IndexImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yzh
 * Date:2021/4/20
 */
@Service
public class IndexImgServiceImpl extends ServiceImpl<IndexImgMapper,IndexImg> implements IndexImgService {
    @Autowired
    private IndexImgMapper indexImgMapper;

    @Override
    public void deleteIndexImgsByIds(Long[] ids) {
        indexImgMapper.deleteIndexImgsByIds(ids);
    }

    @Override
    @Cacheable(cacheNames = "indexImg", key = "'indexImg'")
    public List<IndexImg> listIndexImgs() {
        return indexImgMapper.listIndexImgs();
    }

    @Override
    @CacheEvict(cacheNames = "indexImg", key = "'indexImg'")
    public void removeIndexImgCache() {
    }
}
