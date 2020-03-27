package com.ngnis.walle.center.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 用户
 *
 * @author houyi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    /**
     * 账号
     */
    private String account;

    /**
     * 账号创建时间
     */
    private String gmtCreate;

    /**
     * AccessKey
     */
    private String accessKey;

    /**
     * 上次登录时间
     */
    private String lastLogin;

}
