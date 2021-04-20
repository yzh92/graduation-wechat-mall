package com.shop.security.service;


import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author yzh
 * Date:2021/4/20
 */
@Getter
@Setter
public class ShopSysUser extends User {
    /**
     * 用户ID
     */
    @Getter
    private Long userId;

    /**
     * 租户ID
     */
    @Getter
    private Long shopId;

    public ShopSysUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
}
