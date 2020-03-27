package com.ngnis.walle.web;

import com.ngnis.walle.center.account.*;
import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.PojoResult;
import com.ngnis.walle.core.auth.CheckToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author houyi
 */
@Slf4j
@RestController
@RequestMapping(ApiConstant.Urls.USER)
public class UserController extends BaseController {

    @Resource
    private AccountCenter accountCenter;

    /**
     * 用户注册
     * 只需要用户名和密码
     */
    @PostMapping(ApiConstant.Urls.USER_REGISTER)
    public BaseResult register(@RequestBody RegisterDTO registerDTO) {
        return accountCenter.register(registerDTO);
    }

    /**
     * 用户登录
     * 只需要用户名和密码
     */
    @PostMapping(ApiConstant.Urls.USER_LOGIN)
    public PojoResult<UserDTO> login(HttpServletResponse response, @RequestBody LoginDTO loginDTO) {
        return accountCenter.login(response, loginDTO);
    }

    /**
     * 修改密码
     */
    @CheckToken
    @PostMapping(ApiConstant.Urls.USER_UPDATE_PASSWORD)
    public BaseResult updatePwd(@RequestBody UpdatePwdDTO updatePwdDTO) {
        UpdatePwdDTO newUpdatePwdDTO = newUpdatePwdDTO(updatePwdDTO);
        return accountCenter.updatePwd(newUpdatePwdDTO);
    }

    /**
     * 查询用户信息
     */
    @CheckToken
    @GetMapping(ApiConstant.Urls.GET_USER_INFO)
    public PojoResult<UserDTO> getUserInfo() {
        return accountCenter.getUserInfo(newAccountQueryDTO());
    }

    /**
     * 查询SecurityKey
     */
    @CheckToken
    @PostMapping(ApiConstant.Urls.GET_USER_SK)
    public PojoResult<String> getSecurityKey(@RequestBody QueryDTO queryDTO) {
        QueryDTO newAccountQuery = newAccountQueryDTO(queryDTO);
        return accountCenter.getSecurityKey(newAccountQuery);
    }

    /**
     * 用户注销登录
     */
    @CheckToken
    @PostMapping(ApiConstant.Urls.USER_LOGOUT)
    public BaseResult logout() {
        return accountCenter.logout(newLogoutDTO());
    }


}
