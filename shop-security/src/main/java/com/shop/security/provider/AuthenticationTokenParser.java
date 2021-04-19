package com.shop.security.provider;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * AuthenticationTokenParser
 *
 * @author yzh
 */
public interface AuthenticationTokenParser {
    AbstractAuthenticationToken parse(String authenticationTokenStr);
}
