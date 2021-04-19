package com.shop.security.service;

import com.shop.security.enums.App;
import com.shop.security.exception.UsernameNotFoundExceptionBase;
import com.shop.security.model.AppConnect;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 用户详细信息
 *
 * @author
 */
public interface ShopUserDetailsService extends UserDetailsService {

    /**
     * 获取前端登陆的用户信息
     *
     * @param app 所登陆的应用
     * @param bizUserId openId
     * @return UserDetails
     * @throws UsernameNotFoundExceptionBase
     */
    ShopUser loadUserByAppIdAndBizUserId(App app, String bizUserId);

    /**
     * 如果必要的话，插入新增用户
     * @param appConnect
     */
    void insertUserIfNecessary(AppConnect appConnect);

    /**
     * 账号、密码登录
     * @param userMail
     * @param loginPassword
     * @return
     */
    ShopUser loadUserByUserMail(String userMail, String loginPassword);
}