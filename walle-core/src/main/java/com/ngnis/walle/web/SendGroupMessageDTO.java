package com.ngnis.walle.web;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author houyi
 */
@Data
public class SendGroupMessageDTO {

    @NotBlank(message = "模板编码不能为空")
    private String boardCode;

    private String data;

}
