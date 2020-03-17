package com.ngnis.walle.common;

import lombok.extern.slf4j.Slf4j;

/**
 * @author houyi
 **/
@Slf4j
public class HttpContext {

    private static final ThreadLocal<HttpContext> CONTEXT_HOLDER = new ThreadLocal<>();

    private String token;

    private Long userId;

    private HttpContext() {

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public static HttpContext currentContext() {
        HttpContext context = CONTEXT_HOLDER.get();
        if (context == null) {
            context = new HttpContext();
            CONTEXT_HOLDER.set(context);
        }
        return context;
    }

    public static void clear() {
        CONTEXT_HOLDER.remove();
    }


}
