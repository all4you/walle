package com.ngnis.walle.web;

import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.core.auth.CheckSign;
import com.ngnis.walle.core.robot.Robot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping(ApiConstant.Urls.API)
public class ApiController {

    @Resource
    @Qualifier("walleRobot")
    private Robot robot;

    /**
     * 发送消息
     * 需要对ak/sk进行校验
     */
    @CheckSign
    @PostMapping(ApiConstant.Urls.SEND_GROUP_MSG)
    public BaseResult sendMessage(@RequestBody SendGroupMessageDTO dto) {
        return robot.sendGroupMessage(dto);
    }


}

