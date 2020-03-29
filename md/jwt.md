## JWT

**JSON WEB Token(JWT)**，是一种基于 JSON 的、用于在网络上声明某种主张的令牌(token)。

JWT 通常由三部分组成: 

- 头信息(header)
- 消息体(payload)
- 签名(signature)

头信息指定了该 JWT 使用的签名算法:

```json
header = '{"alg":"HS256","typ":"JWT"}'
```

`HS256` 表示使用了 HMAC-SHA256 来生成签名。

消息体包含了 JWT 的意图：

```json
payload = '{"loggedInAs":"admin","iat":1422779638}' // iat表示令牌生成的时间
```

而签名则是根据 header 和 payload 加密得到，具体的原理如下：

JWT中的数据包括三部分内容：a.b.c
a是header(头部部分)，数据保存在一个叫 headerClaims 的Map中
b是payload(负载部分)，数据保存在一个叫 payloadClaims 的Map中
    payloadClaims可以定义：
    +--标准的Claim，JWT保留的Claim，可以通过类似：withIssuedAt的方法来声明
    +--公共的Claim
    +--私有的Claim，可以声明自定义的Claim，通过方法：addClaim(key, value)来声明
c是signature(签证部分)，由 header，payload，通过 secret加密得到，伪代码如下：

```java
String base64Header = base64(header);
String base64Payload = base64(payload);
Algorithm algorithm = Algorithm(secret);
String signature = algorithm.sign(base64Header, base64Payload);
String base64Signature = base64(signature);
```
最终的 token 由三部分组成：

```java
String jwtToken = String.format("%s.%s.%s", base64Header, base64Payload, base64Signature);
```

JWT令牌管理使用的是 java-jwt 的库：
https://github.com/auth0/java-jwt



### 生成 JWT

用户登录成功之后，为该用户创建一个 JWT，并将用户id、登录时间等非敏感信息保存在 JWT 中：

```java
public String newToken(UserDO user, Date loginDate) {
    String tokenId = IdUtil.objectId();
    // token中保存了部分非敏感信息
    return JWT.create()
            // 设置创建时间
            .withIssuedAt(DateUtil.date())
            // 设置过期时间，1小时
            .withExpiresAt(expireDate())
            // 将部分信息保存到 PayloadClaim 的私有Claim中
            .withClaim("userId", user.getUserId())
            .withClaim("tokenId", tokenId)
            .withClaim("account", user.getAccount())
            .withClaim("gmtCreate", DateUtil.formatDateTime(user.getGmtCreate()))
            .withClaim("accessKey", user.getAccessKey())
            .withClaim("lastLogin", DateUtil.formatDateTime(loginDate))
            // 以 JWT_SECRET 作为 token 的密钥对jwt中的数据进行加密
            .sign(Algorithm.HMAC256(this.secret()));
}
```

生成 jwt 之后，将 jwt 写入 HttpServletResponse 的 header 中：

```java
// 3.生成token
String token = tokenFactory.newToken(userDO, DateUtil.date());
/*
 * 为了防止跨域请求(CORS)，浏览器只能获得几个默认的响应头：
 * <ul>
 *  <li> Cache-Control </li>
 *  <li> Content-Language </li>
 *  <li> Content-Type </li>
 *  <li> Expires </li>
 *  <li> Last-Modified </li>
 *  <li> Pragma </li>
 * </ul>
 * 如果让浏览器能获取其他响应头，需要在响应头中指定需要暴露的响应头
 */
response.addHeader("Access-Control-Expose-Headers", "walle-token");
response.addHeader("walle-token", token);
```

然后前端从响应头中将 jwt 取出，保存在 Cookie 中，并在之后的所有请求中，将 jwt 添加到 Request Header 中。

### 校验 JWT

服务端为了判断每个接口的合法性，需要对请求头中的 token 进行校验，校验通过之后才能进行具体的数据操作。

这部分校验工作可以通过 Spring 的拦截器实现，具体的校验部分，如下所示：

```java
public boolean validToken(String token) {
    JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(this.secret())).build();
    try {
        jwtVerifier.verify(token);
    } catch (JWTVerificationException e) {
        GenericLogUtil.invokeError(log, "validToken", StrFormatter.format("token={}", token), e);
        return false;
    }
    return true;
}
```

但是我们只能通过秘钥来判断 JWT 是否合法，无法做到主动让一个 JWT 失效，比如我们创建了一个 JWT，并设置了1个小时的有效期，在这1个小时之内，都是有效的，哪怕服务端重启了。

### 记录 JWT 的版本号

由于我们无法作废一个服务端颁布的 JWT，因为 JWT 本身只有一个有效期的校验规则。

为了保证服务端生成的 JWT 可以被主动作废，例如在用户注销登录、修改密码之后，我们需要主动将之前颁布给该用户的 JWT 作废。

为了达到这个目的，我们可以为颁布的 JWT 记录一个版本号，当需要作废一个 JWT 时，将关联的版本号删除即可。

在服务端用一个 Map 来记录每个 JWT 的版本号：

```java
key ===> userId#tokenId
val ===> version
```

当需要作废一个 JWT 时，将 Map 中的版本号记录删除。

详细信息可参考 `com.ngnis.walle.core.auth.TokenFactory`

**PS:该方式只适用于单机部署，如果服务端需要分布式部署，则需要将 JWT 的版本号保存在 Redis 或其他公共的存储中心。**





