package com.ngnis.walle.center.account;

import com.ngnis.walle.common.bean.Validate;
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
public class QueryDTO {

    @NotNull(groups = {Validate.QueryAccount.class, Validate.QuerySK.class}, message = "账号不能为空")
    private Long userId;

    @NotBlank(groups = {Validate.QuerySK.class}, message = "密码不能为空")
    private String password;

}
