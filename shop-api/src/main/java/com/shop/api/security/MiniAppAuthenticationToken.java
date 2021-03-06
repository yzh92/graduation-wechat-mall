
package com.shop.api.security;

import com.shop.security.token.MyAuthenticationToken;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 二维码Token
 * @author yzh
 *
 * 生成无参构造函数
 */

@NoArgsConstructor
public class MiniAppAuthenticationToken extends MyAuthenticationToken {
    public MiniAppAuthenticationToken(UserDetails principal, Object credentials) {
        //用户名，密码，用户权限
        super(principal, credentials, principal.getAuthorities());
    }
}
