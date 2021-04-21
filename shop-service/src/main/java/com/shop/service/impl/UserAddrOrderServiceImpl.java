package com.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.bean.model.UserAddrOrder;
import com.shop.dao.UserAddrOrderMapper;
import com.shop.service.UserAddrOrderService;
import org.springframework.stereotype.Service;

@Service
public class UserAddrOrderServiceImpl extends ServiceImpl<UserAddrOrderMapper, UserAddrOrder> implements UserAddrOrderService{

}
