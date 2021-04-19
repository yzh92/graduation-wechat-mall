package com.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.bean.model.User;
import com.shop.bean.param.UserRegisterParam;

/**
 *
 * @author lgh on 2018/09/11.
 */
public interface UserService extends IService<User> {

    User getUserByUserId(String userId);

    Boolean insertUser(UserRegisterParam userRegisterParam);
}
