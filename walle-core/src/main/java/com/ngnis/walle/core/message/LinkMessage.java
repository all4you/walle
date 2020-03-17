package com.ngnis.walle.core.message;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Link类的消息
 * <p>
 *     {
 *          "msgtype": "link",
 *          "link": {
 *              "title": "自定义机器人协议",
 *              "text":"群机器人是钉钉群的高级扩展功能。群机器人可以将第三方服务的信息聚合到群聊中，实现自动化的信息同步。例如：通过聚合GitHub，GitLab等源码管理服务，实现源码更新同步；通过聚合Trello，JIRA等项目协调服务，实现项目信息同步。不仅如此，群机器人支持Webhook协议的自定义接入，支持更多可能性，例如：你可将运维报警提醒通过自定义机器人聚合到钉钉群。",
 *              "messageUrl": "https://open-doc.dingtalk.com/docs/doc.htm?spm=a219a.7629140.0.0.Rqyvqo&treeId=257&articleId=105735&docType=1",
 *              "picUrl": ""
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
public class LinkMessage extends AbstractMessage {

    @NotNull
    @Valid
    private Link link;

    @Override
    public MessageType messageType() {
        return MessageType.LINK;
    }

}

