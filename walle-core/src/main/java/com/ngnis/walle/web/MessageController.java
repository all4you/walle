package com.ngnis.walle.web;

import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.core.auth.CheckToken;
import com.ngnis.walle.core.robot.Robot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 发送消息的api
 *
 * @author houyi.wh
 * @since 2018-09-14
 */
@Slf4j
@RestController
public class MessageController {

    @Resource
    @Qualifier("walleRobot")
    private Robot robot;

    /**
     * 发送消息
     */
    @CheckToken
    @PostMapping(ApiConstant.Urls.SEND_MESSAGE)
    public BaseResult sendMessage(@RequestBody SendGroupMessageDTO dto) {
        return robot.sendGroupMessage(dto);
    }


}

