package com.ngnis.walle.core.user;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrFormatter;
import com.ngnis.walle.common.HttpContext;
import com.ngnis.walle.common.bean.BeanValidator;
import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.PojoResult;
import com.ngnis.walle.common.result.ResultCode;
import com.ngnis.walle.core.auth.TokenFactory;
import com.ngnis.walle.datasource.db.user.UserDO;
import com.ngnis.walle.service.UserService;
import com.ngnis.walle.web.UpdatePwdDTO;
import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.PojoResult;
import com.ngnis.walle.common.result.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

/**
 * 用户门面
 *
 * @author houyi
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserFacade {

    private final UserService userService;

    private final TokenFactory tokenFactory;

    /**
     * 用户注册
     */
    public synchronized BaseResult register(UserRegisterDTO registerDTO) {
        Assert.notNull(registerDTO, "用户信息不能为空");
        BaseResult baseResult = BeanValidator.validate(registerDTO);
        if (!baseResult.isSuccess()) {
            return baseResult;
        }

        String account = registerDTO.getAccount();
        String password = registerDTO.getPassword();
        // 1.检查账号是否存在
        int cnt = userService.getUserCnt(account);
        if (cnt > 0) {
            baseResult.setErrorMessage(ResultCode.RECORD_ALREADY_EXISTS.getCode(), StrFormatter.format("账号({})已存在", account));
            return baseResult;
        }
        // 2.创建用户账号
        String encryptPwd = userService.encryptPwd(password);
        String accessKey = userService.createAccessKey(account);
        String secretKey = userService.createSecretKey(account);
        UserDO userDO = UserDO.builder()
                .account(account)
                .password(encryptPwd)
                .accessKey(accessKey)
                .secretKey(secretKey)
                .build();
        userService.saveUser(userDO);
        return baseResult;
    }

    public synchronized PojoResult<UserDTO> login(HttpServletResponse response, UserLoginDTO loginDTO) {
        Assert.notNull(loginDTO, "用户信息不能为空");
        BaseResult baseResult = BeanValidator.validate(loginDTO);
        PojoResult<UserDTO> pojoResult = new PojoResult<>();
        if (!baseResult.isSuccess()) {
            pojoResult.setErrorMessage(baseResult.getErrorCode(), baseResult.getErrorMsg());
            return pojoResult;
        }
        String account = loginDTO.getAccount();
        String password = loginDTO.getPassword();
        // 1.检查用户是否存在
        UserDO userDO = userService.getUserByAccount(account);
        if (userDO == null) {
            pojoResult.setErrorMessage(ResultCode.RESOURCE_NOT_FOUND.getCode(), "账号不存在");
            return pojoResult;
        }
        // 2.校验密码是否正确
        String encryptPwd = userService.encryptPwd(password);
        if (!encryptPwd.equalsIgnoreCase(userDO.getPassword())) {
            pojoResult.setErrorMessage(ResultCode.RESOURCE_NOT_FOUND.getCode(), "账号或密码不正确");
            return pojoResult;
        }
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
        // 4.创建返回对象
        UserDTO userDTO = buildUserDTO(userDO, token);
        pojoResult.setContent(userDTO);
        return pojoResult;
    }

    public synchronized BaseResult updatePwd(UpdatePwdDTO updatePwdDTO) {
        BaseResult baseResult = BeanValidator.validate(updatePwdDTO);
        if (!baseResult.isSuccess()) {
            return baseResult;
        }
        HttpContext httpContext = HttpContext.currentContext();
        Long userId = httpContext.getUserId();
        // 1.检查用户是否存在
        UserDO userDO = userService.getUserById(userId);
        if (userDO == null) {
            baseResult.setErrorMessage(ResultCode.RESOURCE_NOT_FOUND.getCode(), "账号不存在");
            return baseResult;
        }
        // 2.校验原密码是否正确
        String encryptPwd = userService.encryptPwd(updatePwdDTO.getPassword());
        if (!encryptPwd.equalsIgnoreCase(userDO.getPassword())) {
            baseResult.setErrorMessage(ResultCode.RESOURCE_NOT_FOUND.getCode(), "原密码不正确");
            return baseResult;
        }
        // 3.更新新密码
        String newEncryptPwd = userService.encryptPwd(updatePwdDTO.getNewPassword());
        UserDO updatedUserDO = UserDO.builder()
                .id(userId)
                .password(newEncryptPwd)
                .build();
        userService.updateUser(updatedUserDO);
        // 4.将当前用户下所有的token都删除
        tokenFactory.removeTokenByUserId(userId);
        return baseResult;
    }

    public PojoResult<UserDTO> getUserInfo() {
        PojoResult<UserDTO> pojoResult = new PojoResult<>();
        HttpContext httpContext = HttpContext.currentContext();
        Long userId = httpContext.getUserId();
        // 1.检查用户是否存在
        UserDO userDO = userService.getUserById(userId);
        if (userDO == null) {
            pojoResult.setErrorMessage(ResultCode.RESOURCE_NOT_FOUND.getCode(), "账号不存在");
            return pojoResult;
        }
        // 2.创建返回对象
        UserDTO userDTO = buildUserDTO(userDO, httpContext.getToken());
        pojoResult.setContent(userDTO);
        return pojoResult;
    }

    private UserDTO buildUserDTO(UserDO userDO, String token) {
        return UserDTO.builder()
                .account(userDO.getAccount())
                .gmtCreate(DateUtil.formatDateTime(userDO.getGmtCreate()))
                .accessKey(userDO.getAccessKey())
                .lastLogin(tokenFactory.getLastLogin(token))
                .build();
    }

    public PojoResult<String> getSecurityKey(String password) {
        PojoResult<String> pojoResult = new PojoResult<>();
        HttpContext httpContext = HttpContext.currentContext();
        Long userId = httpContext.getUserId();
        // 1.检查用户是否存在
        UserDO userDO = userService.getUserById(userId);
        if (userDO == null) {
            pojoResult.setErrorMessage(ResultCode.RESOURCE_NOT_FOUND.getCode(), "账号不存在");
            return pojoResult;
        }
        // 2.校验密码是否正确
        String encryptPwd = userService.encryptPwd(password);
        if (!encryptPwd.equalsIgnoreCase(userDO.getPassword())) {
            pojoResult.setErrorMessage(ResultCode.RESOURCE_NOT_FOUND.getCode(), "密码不正确");
            return pojoResult;
        }
        // 1.返回SecurityKey
        String securityKey = userDO.getSecretKey();
        pojoResult.setContent(securityKey);
        return pojoResult;
    }

    public BaseResult logout() {
        // 将当前登录的token置为失效
        String token = HttpContext.currentContext().getToken();
        String tokenId = tokenFactory.getTokenId(token);
        tokenFactory.removeTokenByTokenId(tokenId);
        return new BaseResult();
    }

}
