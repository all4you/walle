package com.ngnis.walle.service.account;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrFormatter;
import com.ngnis.walle.center.account.*;
import com.ngnis.walle.common.BeanUtil;
import com.ngnis.walle.common.HttpContext;
import com.ngnis.walle.common.bean.BeanValidator;
import com.ngnis.walle.common.bean.Validate;
import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.PojoResult;
import com.ngnis.walle.common.result.ResultCode;
import com.ngnis.walle.core.auth.TokenFactory;
import com.ngnis.walle.datasource.db.user.UserDO;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;

/**
 * 默认账号中心
 *
 * @author houyi
 */
@Slf4j
public class DefaultAccountCenter implements AccountCenter {

    private AccountService accountService;

    private TokenFactory tokenFactory;

    public DefaultAccountCenter() {
        this.accountService = BeanUtil.getBean(AccountService.class);
        this.tokenFactory = BeanUtil.getBean(TokenFactory.class);
    }

    /**
     * 用户注册
     */
    @Override
    public synchronized BaseResult register(RegisterDTO registerDTO) {
        Assert.notNull(registerDTO, "用户信息不能为空");
        BaseResult baseResult = BeanValidator.validate(registerDTO);
        if (!baseResult.isSuccess()) {
            return baseResult;
        }

        String account = registerDTO.getAccount();
        String password = registerDTO.getPassword();
        // 1.检查账号是否存在
        int cnt = accountService.getUserCnt(account);
        if (cnt > 0) {
            baseResult.setErrorMessage(ResultCode.RECORD_ALREADY_EXISTS.getCode(), StrFormatter.format("账号({})已存在", account));
            return baseResult;
        }
        // 2.创建用户账号
        String encryptPwd = accountService.encryptPwd(password);
        String accessKey = accountService.createAccessKey(account);
        String secretKey = accountService.createSecretKey(account);
        UserDO userDO = UserDO.builder()
                .account(account)
                .password(encryptPwd)
                .accessKey(accessKey)
                .secretKey(secretKey)
                .build();
        accountService.saveUser(userDO);
        return baseResult;
    }

    @Override
    public synchronized PojoResult<UserDTO> login(HttpServletResponse response, LoginDTO loginDTO) {
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
        UserDO userDO = accountService.getUserByAccount(account);
        if (userDO == null) {
            pojoResult.setErrorMessage(ResultCode.RESOURCE_NOT_FOUND.getCode(), "账号不存在");
            return pojoResult;
        }
        // 2.校验密码是否正确
        String encryptPwd = accountService.encryptPwd(password);
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

    @Override
    public BaseResult logout(LogoutDTO logoutDTO) {
        // 将当前登录的token置为失效
        String tokenId = tokenFactory.getTokenId(HttpContext.currentContext().getToken());
        tokenFactory.removeTokenByTokenId(tokenId);
        return new BaseResult();
    }

    @Override
    public synchronized BaseResult updatePwd(UpdatePwdDTO updatePwdDTO) {
        BaseResult baseResult = BeanValidator.validate(updatePwdDTO);
        if (!baseResult.isSuccess()) {
            return baseResult;
        }
        Long userId = updatePwdDTO.getUserId();
        // 1.检查用户是否存在
        UserDO userDO = accountService.getUserById(userId);
        if (userDO == null) {
            baseResult.setErrorMessage(ResultCode.RESOURCE_NOT_FOUND.getCode(), "账号不存在");
            return baseResult;
        }
        // 2.校验原密码是否正确
        String encryptPwd = accountService.encryptPwd(updatePwdDTO.getPassword());
        if (!encryptPwd.equalsIgnoreCase(userDO.getPassword())) {
            baseResult.setErrorMessage(ResultCode.RESOURCE_NOT_FOUND.getCode(), "原密码不正确");
            return baseResult;
        }
        // 3.更新新密码
        String newEncryptPwd = accountService.encryptPwd(updatePwdDTO.getNewPassword());
        UserDO updatedUserDO = UserDO.builder()
                .id(userId)
                .password(newEncryptPwd)
                .build();
        accountService.updateUser(updatedUserDO);
        // 4.将当前用户下所有的token都删除
        tokenFactory.removeTokenByUserId(userId);
        return baseResult;
    }

    @Override
    public PojoResult<UserDTO> getUserInfo(QueryDTO queryDTO) {
        Assert.notNull(queryDTO, "用户信息不能为空");
        BaseResult baseResult = BeanValidator.validate(queryDTO, Validate.QueryAccount.class);
        PojoResult<UserDTO> pojoResult = new PojoResult<>();
        if (!baseResult.isSuccess()) {
            pojoResult.setErrorMessage(baseResult.getErrorCode(), baseResult.getErrorMsg());
            return pojoResult;
        }
        // 1.检查用户是否存在
        UserDO userDO = accountService.getUserById(queryDTO.getUserId());
        if (userDO == null) {
            pojoResult.setErrorMessage(ResultCode.RESOURCE_NOT_FOUND.getCode(), "账号不存在");
            return pojoResult;
        }
        // 2.创建返回对象
        UserDTO userDTO = buildUserDTO(userDO, HttpContext.currentContext().getToken());
        pojoResult.setContent(userDTO);
        return pojoResult;
    }


    @Override
    public PojoResult<String> getSecurityKey(QueryDTO queryDTO) {
        Assert.notNull(queryDTO, "用户信息不能为空");
        BaseResult baseResult = BeanValidator.validate(queryDTO, Validate.QuerySK.class);
        PojoResult<String> pojoResult = new PojoResult<>();
        if (!baseResult.isSuccess()) {
            pojoResult.setErrorMessage(baseResult.getErrorCode(), baseResult.getErrorMsg());
            return pojoResult;
        }
        // 1.检查用户是否存在
        UserDO userDO = accountService.getUserById(queryDTO.getUserId());
        if (userDO == null) {
            pojoResult.setErrorMessage(ResultCode.RESOURCE_NOT_FOUND.getCode(), "账号不存在");
            return pojoResult;
        }
        // 2.校验密码是否正确
        String encryptPwd = accountService.encryptPwd(queryDTO.getPassword());
        if (!encryptPwd.equals(userDO.getPassword())) {
            pojoResult.setErrorMessage(ResultCode.RESOURCE_NOT_FOUND.getCode(), "密码不正确");
            return pojoResult;
        }
        // 1.返回SecurityKey
        String securityKey = userDO.getSecretKey();
        pojoResult.setContent(securityKey);
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

}
