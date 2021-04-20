package com.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.bean.model.User;
import com.shop.bean.param.UserRegisterParam;

/**
 *
 * @author yzh
 */
public interface UserService extends IService<User> {
    /**
     * 根据用户id查询用户
     * @param userId
     * @return
     */
    User getUserByUserId(String userId);

    /**
     * 新增一个用户
     * @param userRegisterParam
     * @return
     */
    Boolean insertUser(UserRegisterParam userRegisterParam);
}
