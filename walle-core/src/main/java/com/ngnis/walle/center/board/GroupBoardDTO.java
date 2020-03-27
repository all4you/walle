package com.ngnis.walle.center.board;

import com.ngnis.walle.core.message.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 要发送的消息模板
 *
 * @author houyi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupBoardDTO {

    private String boardId;

    private Long userId;

    /**
     * 模板编码
     */
    @NotBlank(message = "模板编码不能为空")
    private String boardCode;

    /**
     * 模板名称
     */
    @NotBlank(message = "模板名称不能为空")
    private String boardName;

    /**
     * 消息类型
     */
    @NotNull(message = "消息类型不能为空")
    private MessageType messageType;

    /**
     * 消息标题
     * 消息类型为：link，markdown时，标题不能为空
     */
    private String title;

    /**
     * 消息正文
     * 消息类型为：link，markdown，text时，标题正文不能为空
     */
    private String content;

    /**
     * link消息的跳转链接
     * 消息类型为：link时，跳转链接不能为空
     */
    private String linkMessageUrl;

    /**
     * link消息的配图链接
     */
    private String linkPicUrl;

    /**
     * text和markdown消息，是否艾特所有人
     * 0:否 1：是
     */
    private Byte atAll;

    /**
     * 要将消息发送到的地址列表
     */
    @NotEmpty(message = "要发送的地址不能为空")
    @Valid
    private List<AddressDTO> addresses;


}
