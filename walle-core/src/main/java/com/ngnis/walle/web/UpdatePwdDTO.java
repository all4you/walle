package com.ngnis.walle.web;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author houyi
 */
@Data
public class UpdatePwdDTO {

    @NotBlank(message = "原密码不能为空")
    private String password;

    @NotBlank(message = "新密码不能为空")
    private String newPassword;

}
