package com.ngnis.walle.core.auth;

import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.ngnis.walle.common.GenericLogUtil;
import com.ngnis.walle.common.HttpContext;
import com.ngnis.walle.common.result.ResultCode;
import com.ngnis.walle.core.SignatureUtil;
import com.ngnis.walle.datasource.db.user.UserDO;
import com.ngnis.walle.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 权限校验认证拦截器
 *
 * @author houyi
 */
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Resource
    private TokenFactory tokenFactory;

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        // 检查是否需要认证token
        if (method.isAnnotationPresent(CheckToken.class)) {
            return checkToken(httpServletRequest);
        }

        // 检查是否需要认证sign
        if (method.isAnnotationPresent(CheckSign.class)) {
            return checkSign(httpServletRequest);
        }

        return true;
    }

    private boolean checkToken(HttpServletRequest httpServletRequest) {
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("walle-token");
        // 执行认证
        if (token == null) {
            GenericLogUtil.invokeFail(log, "checkToken", "", "token is null");
            throw new InvalidTokenException(ResultCode.INVALID_TOKEN.getCode(), "登录态失效，请重新登录");
        }
        // 验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(tokenFactory.secret())).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            GenericLogUtil.invokeError(log, "checkToken", StrFormatter.format("token={}", token), e);
            throw new InvalidTokenException(ResultCode.INVALID_TOKEN.getCode(), "登录态失效，请重新登录");
        }
        // 获取 token 中的 userId
        Long userId = tokenFactory.getUserId(token);
        if (userId == null) {
            GenericLogUtil.invokeFail(log, "checkToken", StrFormatter.format("token={}", token), "invalid token");
            throw new InvalidTokenException(ResultCode.INVALID_TOKEN.getCode(), "登录态失效，请重新登录");
        }
        // 判断token的版本号是否有效
        if (!tokenFactory.validTokenVersion(token)) {
            GenericLogUtil.invokeFail(log, "checkToken", StrFormatter.format("token={}", token), "invalid token version");
            throw new InvalidTokenException(ResultCode.INVALID_TOKEN.getCode(), "登录态失效，请重新登录");
        }

        // 将当前用户的信息写入ThreadLocal中
        HttpContext httpContext = HttpContext.currentContext();
        httpContext.setToken(token);
        httpContext.setUserId(userId);
        GenericLogUtil.debugSuccess(log, "storeUserInfo", StrFormatter.format("token={}", token), StrFormatter.format("userId={}", userId));

        return true;
    }


    private boolean checkSign(HttpServletRequest httpServletRequest) {
        // 从 http 请求头中取出 accessKey
        String accessKey = httpServletRequest.getHeader("accessKey");
        String timestamp = httpServletRequest.getHeader("timestamp");
        String sign = httpServletRequest.getHeader("sign");
        if (StrUtil.isBlank(accessKey)) {
            GenericLogUtil.invokeFail(log, "checkSign", "", "accessKey is null");
            throw new InvalidSignException(ResultCode.PARAM_INVALID.getCode(), "参数不正确");
        }
        if (StrUtil.isBlank(timestamp)) {
            GenericLogUtil.invokeFail(log, "checkSign", "", "timestamp is null");
            throw new InvalidSignException(ResultCode.PARAM_INVALID.getCode(), "参数不正确");
        }
        if (StrUtil.isBlank(sign)) {
            GenericLogUtil.invokeFail(log, "checkSign", "", "sign is null");
            throw new InvalidSignException(ResultCode.PARAM_INVALID.getCode(), "参数不正确");
        }
        // 根据 accessKey 查询用户信息
        UserDO userDO = userService.getUserByAccessKey(accessKey);
        if (userDO == null) {
            GenericLogUtil.invokeFail(log, "checkSign", "", "userDO is null");
            throw new InvalidSignException(ResultCode.PARAM_INVALID.getCode(), "参数不正确");
        }
        String signed = SignatureUtil.sign(Long.valueOf(timestamp), userDO.getSecretKey());
        if (!sign.equals(signed)) {
            GenericLogUtil.invokeFail(log, "checkSign", "", "sign is not match");
            throw new InvalidSignException(ResultCode.PARAM_INVALID.getCode(), "签名不正确");
        }
        Long userId = userDO.getId();
        // 将当前用户的信息写入ThreadLocal中
        HttpContext httpContext = HttpContext.currentContext();
        httpContext.setUserId(userId);
        GenericLogUtil.debugSuccess(log, "storeUserInfo", StrFormatter.format("accessKey={}", accessKey), StrFormatter.format("userId={}", userId));

        return true;
    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        HttpContext.clear();
        GenericLogUtil.debugSuccess(log, "clearUserInfo", o.toString(), "");
    }

}