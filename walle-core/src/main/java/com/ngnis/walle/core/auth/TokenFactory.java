package com.ngnis.walle.core.auth;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.ngnis.walle.common.log.GenericLogUtil;
import com.ngnis.walle.center.account.UserDTO;
import com.ngnis.walle.datasource.db.user.UserDO;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Token工厂，生成新的token
 * JWT令牌管理使用的是 java-jwt 的库：
 * https://github.com/auth0/java-jwt
 *
 * JWT中的数据包括三部分内容：a.b.c
 * a是header(头部部分)，数据保存在一个叫 headerClaims 的Map中
 * b是payload(负载部分)，数据保存在一个叫 payloadClaims 的Map中
 *  +-payloadClaims可以定义：
 *  +--标准的Claim，JWT保留的Claim，可以通过类似：withIssuedAt的方法来声明
 *  +--公共的Claim
 *  +--私有的Claim，可以声明自定义的Claim，通过方法：addClaim(key, value)来声明
 * c是signature(签证部分)，由header，payload，secret加密得到：
 * <code>
 *  String base64Header = base64(header);
 *  String base64Payload = base64(payload);
 *  Algorithm algorithm = Algorithm(secret);
 *  String signature = algorithm.sign(base64Header, base64Payload);
 *  String base64Signature = base64(signature);
 * </code>
 * 最终的token由三部分组成：
 * <code>
 *  String jwtToken = String.format("%s.%s.%s", base64Header, base64Payload, base64Signature);
 * </code>
 *
 *
 *
 * @author houyi
 */
@Slf4j
@Service
public class TokenFactory {

    /**
     * 记录所有token的版本号
     * key      :   userId#tokenId
     * value    :   version
     */
    private Map<String, Integer> userTokenVersions = new ConcurrentHashMap<>();

    private static Integer VALID_VERSION = 1;

    private static Integer INVALID_VERSION = 2;

    public String secret() {
        return "&&$_walle_jwt_secret_$&&";
    }

    private Date expireDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR , 1);
        return calendar.getTime();
    }

    public String newToken(UserDO user, Date loginDate) {
        Long userId = user.getId();
        String tokenId = IdUtil.objectId();
        String key = StrFormatter.format("{}#{}", userId, tokenId);
        Integer tokenVersion = userTokenVersions.get(key);
        // 设置该token的版本号为 VALID_VERSION，表示是合法的token
        if (tokenVersion == null) {
            tokenVersion = VALID_VERSION;
        }
        // TODO 这里需要对保存的token进行清理，当失效时间到达时自动清理掉
        userTokenVersions.putIfAbsent(key, tokenVersion);
        // token中保存了部分非敏感信息
        return JWT.create()
                // 设置创建时间
                .withIssuedAt(DateUtil.date())
                // 设置过期时间，1小时
                .withExpiresAt(expireDate())
                // 将部分信息保存到 PayloadClaim 的私有Claim中
                .withClaim("userId", userId)
                .withClaim("tokenId", tokenId)
                .withClaim("account", user.getAccount())
                .withClaim("gmtCreate", DateUtil.formatDateTime(user.getGmtCreate()))
                .withClaim("accessKey", user.getAccessKey())
                .withClaim("lastLogin", DateUtil.formatDateTime(loginDate))
                // 以 JWT_SECRET 作为 token 的密钥对jwt中的数据进行加密
                .sign(Algorithm.HMAC256(this.secret()));
    }

    public boolean validTokenVersion(String token) {
        Map<String, Claim> claimMap = getClaims(token);
        if (CollectionUtil.isEmpty(claimMap)) {
            return false;
        }
        Long userId = Optional.ofNullable(claimMap.get("userId")).map(Claim::asLong).orElse(null);
        String tokenId = Optional.ofNullable(claimMap.get("tokenId")).map(Claim::asString).orElse(null);
        if (userId == null || StrUtil.isBlank(tokenId)) {
            return false;
        }
        String key = StrFormatter.format("{}#{}", userId, tokenId);
        Integer tokenVersion = userTokenVersions.get(key);
        // 当前token的版本号必须等于 VALID_VERSION
        return tokenVersion != null && tokenVersion.equals(VALID_VERSION);
    }

    /**
     * 将该用户下所有以 userId# 开头的token都删除
     */
    public void removeTokenByUserId(Long userId) {
        String prefixKey = StrFormatter.format("{}#", userId);
        userTokenVersions.keySet().removeIf(key -> key.startsWith(prefixKey));
    }

    /**
     * 将所有以 #tokenId 结尾的token都删除
     */
    public void removeTokenByTokenId(String tokenId) {
        String suffixKey = StrFormatter.format("#{}", tokenId);
        userTokenVersions.keySet().removeIf(key -> key.endsWith(suffixKey));
    }

    private Map<String, Claim> getClaims(String token) {
        Map<String, Claim> claimMap = new HashMap<>();
        try {
            claimMap = JWT.decode(token).getClaims();
        } catch (JWTDecodeException e) {
            GenericLogUtil.invokeError(log, "getClaims", StrFormatter.format("token={}", token), e);
        }
        return claimMap;
    }

    public Long getUserId(String token) {
        Map<String, Claim> claimMap = getClaims(token);
        return Optional.ofNullable(claimMap.get("userId")).map(Claim::asLong).orElse(null);
    }

    public String getLastLogin(String token) {
        Map<String, Claim> claimMap = getClaims(token);
        return Optional.ofNullable(claimMap.get("lastLogin")).map(Claim::asString).orElse(null);
    }

    public String getTokenId(String token) {
        Map<String, Claim> claimMap = getClaims(token);
        return Optional.ofNullable(claimMap.get("tokenId")).map(Claim::asString).orElse(null);
    }

    public UserDTO decodeUserDTO(String token) {
        UserDTO userDTO = null;
        try {
            Map<String, Claim> payloadClaim = JWT.decode(token).getClaims();
            userDTO = UserDTO.builder()
                    .account(Optional.ofNullable(payloadClaim.get("account")).map(Claim::asString).orElse(null))
                    .accessKey(Optional.ofNullable(payloadClaim.get("accessKey")).map(Claim::asString).orElse(null))
                    .gmtCreate(Optional.ofNullable(payloadClaim.get("gmtCreate")).map(Claim::asString).orElse(null))
                    .lastLogin(Optional.ofNullable(payloadClaim.get("lastLogin")).map(Claim::asString).orElse(null))
                    .build();
        } catch (JWTDecodeException e) {
            GenericLogUtil.invokeError(log, "decodeUserDTO", StrFormatter.format("token={}", token), e);
        }
        return userDTO;
    }

}