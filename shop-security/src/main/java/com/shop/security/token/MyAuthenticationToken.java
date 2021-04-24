package com.shop.security.token;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 自定义AbstractAuthenticationToken，
 * @author yzh
 */
@Getter
@Setter
public class MyAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 110L;
    protected Object principal;
    protected Object credentials;
    protected Boolean debugger;

    public MyAuthenticationToken() {
        super(null);
    }
    /**
     *此构造函数只能被AuthenticationManager或AuthenticationProvider使用
     *满足于产生一个受信任的实现(例如{@link #isAuthenticated()} = true)
     *令牌
     *
     * @param principal
     * @param credentials
     * @param authorities
     */
    public MyAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }
    public MyAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.setAuthenticated(false);
    }


    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if(isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }

}
