package com.ngnis.walle.web;

import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.PojoResult;
import com.ngnis.walle.core.auth.CheckToken;
import com.ngnis.walle.core.user.UserDTO;
import com.ngnis.walle.core.user.UserFacade;
import com.ngnis.walle.core.user.UserLoginDTO;
import com.ngnis.walle.core.user.UserRegisterDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author houyi
 */
@Slf4j
@RestController // @Controller and @ResponseBody
@RequestMapping(ApiConstant.Urls.USER)
public class UserController {

    @Resource
    private UserFacade userFacade;

    /**
     * 用户注册
     * 只需要用户名和密码
     */
    @PostMapping(ApiConstant.Urls.USER_REGISTER)
    public BaseResult register(@RequestBody UserRegisterDTO registerDTO) {
        return userFacade.register(registerDTO);
    }

    /**
     * 用户登录
     * 只需要用户名和密码
     */
    @PostMapping(ApiConstant.Urls.USER_LOGIN)
    public PojoResult<UserDTO> login(HttpServletResponse response, @RequestBody UserLoginDTO loginDTO) {
        return userFacade.login(response, loginDTO);
    }

    /**
     * 修改密码
     */
    @CheckToken
    @PostMapping(ApiConstant.Urls.USER_UPDATE_PASSWORD)
    public BaseResult updatePwd(@RequestBody UpdatePwdDTO updatePwdDTO) {
        return userFacade.updatePwd(updatePwdDTO);
    }

    /**
     * 查询用户信息
     */
    @CheckToken
    @GetMapping(ApiConstant.Urls.GET_USER_INFO)
    public PojoResult<UserDTO> getUserInfo() {
        return userFacade.getUserInfo();
    }

    /**
     * 查询SecurityKey
     */
    @CheckToken
    @PostMapping(ApiConstant.Urls.GET_USER_SK)
    public PojoResult<String> getSecurityKey(@RequestBody SkQueryDTO queryDTO) {
        return userFacade.getSecurityKey(queryDTO.getPassword());
    }

    /**
     * 用户注销登录
     */
    @CheckToken
    @PostMapping(ApiConstant.Urls.USER_LOGOUT)
    public BaseResult logout() {
        return userFacade.logout();
    }


}
