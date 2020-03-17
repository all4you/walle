package com.ngnis.walle.core.message;

import lombok.Getter;

/**
 * @author houyi.wh
 * @since 2018-09-09
 */
@Getter
public enum MessageType {

    /**
     * 文本
     */
    TEXT("text",(byte)1),
    /**
     * 链接
     */
    LINK("link",(byte)2),
    /**
     * markdown
     */
    MARKDOWN("markdown",(byte)3)
    ;

    MessageType(String type,byte value){
        this.type = type;
        this.value = value;
    }

    private String type;

    private byte value;

}
