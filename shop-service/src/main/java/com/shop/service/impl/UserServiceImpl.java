package com.shop.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.bean.model.User;
import com.shop.bean.param.UserRegisterParam;
import com.shop.common.exception.ShopBindException;
import com.shop.dao.UserMapper;
import com.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author yzh
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    @Cacheable(cacheNames = "user", key = "#userId")
    public User getUserByUserId(String userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public Boolean insertUser(UserRegisterParam uParam) {
        User mail = userMapper.getUserByUserMail(uParam.getUserMail());
        if (mail != null) {
            throw new ShopBindException("账号已存在");
        }
        Date now = new Date();
        User user = new User();
        user.setUserId(IdUtil.simpleUUID());
        user.setModifyTime(now);
        user.setUserRegtime(now);
        user.setStatus(1);
        user.setNickName(uParam.getUserMail());
        user.setUserMail(uParam.getUserMail());
        user.setLoginPassword(uParam.getPassword());
        return userMapper.insert(user) == 1;
    }
}
