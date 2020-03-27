package com.ngnis.walle.center.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author houyi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePwdDTO {

    @NotNull(message = "账号不能为空")
    private Long userId;

    @NotBlank(message = "原密码不能为空")
    private String password;

    @NotBlank(message = "新密码不能为空")
    private String newPassword;

}
