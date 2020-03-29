package com.ngnis.walle.center.msg;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author houyi
 */
@Data
public class SendGroupMessageDTO {

    @NotNull(message = "用户id不能为空")
    private Long userId;

    @NotBlank(message = "模板编码不能为空")
    private String boardCode;

    private String data;

}
