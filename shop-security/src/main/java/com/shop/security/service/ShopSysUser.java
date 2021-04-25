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

    public ShopSysUser(Long userId, Long shopId, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId = userId;
        this.shopId = shopId;
    }
}
