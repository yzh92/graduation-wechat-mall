package com.shop.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.security.dao.AppConnectMapper;
import com.shop.security.enums.App;
import com.shop.security.model.AppConnect;
import com.shop.security.service.AppConnectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


/**
 * @author yzh
 */
@Service
public class AppConnectServiceImpl extends ServiceImpl<AppConnectMapper, AppConnect> implements AppConnectService {
    @Autowired
    private AppConnectMapper appConnectMapper;

    @Override
    @Cacheable(cacheNames = "AppConnect", key = "#app.value() + ':' + #bizUserId")
    public AppConnect getByBizUserId(String bizUserId, App app) {
        return appConnectMapper.getByBizUserId(bizUserId,app.value());
    }


}

