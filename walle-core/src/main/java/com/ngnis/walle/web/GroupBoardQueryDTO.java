package com.ngnis.walle.web;

import com.ngnis.walle.common.page.PageDTO;
import com.ngnis.walle.core.message.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author houyi
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupBoardQueryDTO extends PageDTO {

    private Long userId;

    private String query;

    private MessageType messageType;

}
