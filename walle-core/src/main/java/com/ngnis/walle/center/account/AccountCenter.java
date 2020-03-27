package com.ngnis.walle.center.account;

import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.PojoResult;

import javax.servlet.http.HttpServletResponse;

/**
 * 账号工厂
 *
 * @author houyi
 */
public interface AccountCenter {

    /**
     * 注册
     */
    BaseResult register(RegisterDTO registerDTO);

    /**
     * 登录
     */
    PojoResult<UserDTO> login(HttpServletResponse response, LoginDTO loginDTO);

    /**
     * 注销
     */
    BaseResult logout(LogoutDTO logoutDTO);

    /**
     * 改密
     */
    BaseResult updatePwd(UpdatePwdDTO updatePwdDTO);

    /**
     * 获取账号详情
     */
    PojoResult<UserDTO> getUserInfo(QueryDTO queryDTO);

    /**
     * 获取SK
     */
    PojoResult<String> getSecurityKey(QueryDTO queryDTO);


}
