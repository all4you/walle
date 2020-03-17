package com.ngnis.walle.core.message;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Markdown类的消息
 * <p>
 *     {
 *          "msgtype": "markdown",
 *          "markdown": {
 *              "title":"杭州天气",
 *              "text":"#### 杭州天气  \n > 9度，@1825718XXXX 西北风1级，空气良89，相对温度73%\n\n > ![screenshot](http://i01.lw.aliimg.com/media/lALPBbCc1ZhJGIvNAkzNBLA_1200_588.png)\n  > ###### 10点20分发布 [天气](http://www.thinkpage.cn/) "
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
public class MarkdownMessage extends AbstractMessage {

    @NotNull
    @Valid
    private Markdown markdown;

    private At at;

    @Override
    public MessageType messageType() {
        return MessageType.MARKDOWN;
    }


}

