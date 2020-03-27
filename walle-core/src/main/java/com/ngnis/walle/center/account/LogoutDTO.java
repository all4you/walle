package com.ngnis.walle.center.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author houyi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogoutDTO {

    @NotNull(message = "账号不能为空")
    private Long userId;

}
