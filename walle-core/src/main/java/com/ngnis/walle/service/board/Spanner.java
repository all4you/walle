package com.ngnis.walle.service.board;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.ngnis.walle.center.board.GroupBoardDTO;
import com.ngnis.walle.common.VelocityUtil;
import com.ngnis.walle.common.bean.BeanValidator;
import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.ResultCode;
import com.ngnis.walle.core.message.*;
import com.ngnis.walle.core.message.MarkdownMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author houyi
 */
@Component
public class Spanner {

    public BaseResult check(GroupBoardDTO board) {
        Assert.notNull(board, "模板不能为空");
        BaseResult baseResult = BeanValidator.validate(board);
        if (!baseResult.isSuccess()) {
            return baseResult;
        }
        MessageType messageType = board.getMessageType();
        String title = board.getTitle();
        String content = board.getContent();
        if (StrUtil.isBlank(content)) {
            baseResult.setErrorMessage(ResultCode.PARAM_INVALID.getCode(), "正文(content)不能为空");
            return baseResult;
        }
        switch (messageType) {
            case LINK: {
                if (StrUtil.isBlank(title)) {
                    baseResult.setErrorMessage(ResultCode.PARAM_INVALID.getCode(), "标题(title)不能为空");
                }
                String linkMessageUrl = board.getLinkMessageUrl();
                if (StrUtil.isBlank(linkMessageUrl)) {
                    baseResult.setErrorMessage(ResultCode.PARAM_INVALID.getCode(), "跳转链接(linkMessageUrl)不能为空");
                }
            }
            break;
            case MARKDOWN: {
                if (StrUtil.isBlank(title)) {
                    baseResult.setErrorMessage(ResultCode.PARAM_INVALID.getCode(), "标题(title)不能为空");
                }
            }
            break;
            default:
                break;
        }
        return baseResult;
    }

    public Message make(GroupBoardDTO board, Map<String, Object> data) {
        Assert.notNull(board, "模板不能为空");
        MessageType messageType = board.getMessageType();
        String boardCode = board.getBoardCode();
        // 标题渲染
        String title = VelocityUtil.render(boardCode, board.getTitle(), data);
        // 正文渲染
        String content = VelocityUtil.render(boardCode, board.getContent(), data);
        return buildMessage(messageType, title, content, board.getLinkMessageUrl(), board.getLinkPicUrl(), board.getAtAll());
    }

    private Message buildMessage(MessageType messageType, String title, String content, String linkMessageUrl, String linkPicUrl, Byte atAll) {
        Message message;
        if (messageType == MessageType.LINK) {
            Link link = Link.builder()
                    .title(title)
                    .text(content)
                    .messageUrl(linkMessageUrl)
                    .picUrl(linkPicUrl)
                    .build();
            message = LinkMessage.builder()
                    .link(link)
                    .build();
        } else {
            // 只有text和markdown类型的消息，才能艾特用户
            List<String> atMobiles = extractAtMobiles(content);
            Byte doAtAll = (byte) 1;
            boolean isAtAll = doAtAll.equals(atAll);
            At at = At.builder()
                    .atMobiles(atMobiles)
                    .isAtAll(isAtAll)
                    .build();
            if (messageType == MessageType.MARKDOWN) {
                Markdown markdown = Markdown.builder()
                        .title(title)
                        .text(content)
                        .build();
                message = MarkdownMessage.builder()
                        .markdown(markdown)
                        .at(at)
                        .build();
            } else {
                Text text = Text.builder()
                        .content(content)
                        .build();
                message = TextMessage.builder()
                        .text(text)
                        .at(at)
                        .build();
            }
        }
        return message;
    }

    private List<String> extractAtMobiles(String originContent) {
        return ReUtil.findAll("@(\\d{11})", originContent, 1);
    }

    public static void main(String[] args) {
        String originContent = "hi@13813381888，我是@12334554234，灌灌灌灌##";
        List<String> atMobiles = ReUtil.findAll("@(\\d{11})", originContent, 1);
        System.out.println(atMobiles);
    }

}
