package com.ngnis.walle.core.message;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Text类的消息
 * <p>
 *     {
 *          "msgtype": "text",
 *          "text": {
 *              "content": "我就是我,  @1825718XXXX 是不一样的烟火"
 *          },
 *          "at": {
 *              "atMobiles": [
 *                  "1825718XXXX"
 *              ],
 *              "isAtAll": false
 *          }
 *     }
 * </p>
 * @author houyi.wh
 * @since 2018-09-09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextMessage extends AbstractMessage {

    @NotNull
    @Valid
    private Text text;

    private At at;

    @Override
    public MessageType messageType() {
        return MessageType.TEXT;
    }

}

