package com.carepay.blog;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RestAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authEx) {
        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");
        response.setStatus(HttpStatus.FORBIDDEN.value());
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("carepay");
        super.afterPropertiesSet();
    }

}
