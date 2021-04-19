package com.shop.security.service;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

/**
 * 小程序用户详细信息
 * @author yzh
 */
@Getter
public class ShopUser extends User {

    /**
     * 用户ID
     */
    @Setter
    private String userId;

    @Setter
    private String bizUserId;

    @Setter
    private String pic;

    @Setter
    private String name;

    @Setter
    private boolean debugger;

    @Setter
    private String password;

    public ShopUser(String userId, String password, boolean enabled) {
        super(userId, password, enabled,true, true, true , Collections.emptyList());
        this.userId = userId;
        this.password = password;
    }

    public ShopUser(String userId, String bizUserId, Integer appId, boolean enabled) {
        super(appId + StrUtil.COLON + bizUserId, "", enabled,true, true, true , Collections.emptyList());
        this.userId = userId;
        this.bizUserId = bizUserId;
    }
}
