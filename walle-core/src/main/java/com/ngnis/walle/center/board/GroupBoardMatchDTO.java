package com.ngnis.walle.center.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 要发送的消息模板
 *
 * @author houyi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupBoardMatchDTO {

    private Long userId;

    /**
     * 模板编码
     */
    @NotBlank(message = "模板编码不能为空")
    private String boardCode;


}
