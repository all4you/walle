package com.ngnis.walle.service.msg;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.ngnis.walle.center.board.AddressDTO;
import com.ngnis.walle.center.board.GroupBoardCenter;
import com.ngnis.walle.center.board.GroupBoardDTO;
import com.ngnis.walle.center.board.GroupBoardMatchDTO;
import com.ngnis.walle.center.msg.MessageCenter;
import com.ngnis.walle.center.msg.SendGroupMessageDTO;
import com.ngnis.walle.common.bean.BeanValidator;
import com.ngnis.walle.common.log.PrintLog;
import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.PojoResult;
import com.ngnis.walle.common.result.ResultCode;
import com.ngnis.walle.core.message.Message;
import com.ngnis.walle.core.sender.DingTalkResponse;
import com.ngnis.walle.core.sender.Sender;
import com.ngnis.walle.service.board.Spanner;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author houyi
 */
@Slf4j
public class DefaultMessageCenter implements MessageCenter {

    private GroupBoardCenter groupBoardCenter;

    private Spanner spanner;

    private Sender sender;

    public DefaultMessageCenter(GroupBoardCenter groupBoardCenter, Spanner spanner, Sender sender) {
        this.groupBoardCenter = groupBoardCenter;
        this.spanner = spanner;
        this.sender = sender;
    }


    @PrintLog
    @Override
    @SuppressWarnings("unchecked")
    public BaseResult sendGroupMessage(SendGroupMessageDTO messageDTO) {
        BaseResult baseResult = BeanValidator.validate(messageDTO);
        if (!baseResult.isSuccess()) {
            return baseResult;
        }
        Map<String, Object> data = new HashMap<>();
        if (StrUtil.isNotBlank(messageDTO.getData())) {
            try {
                data = JSON.parseObject(messageDTO.getData(), Map.class);
            } catch (Exception e) {
                baseResult.setErrorMessage(ResultCode.PARAM_INVALID.getCode(), "携带的数据只能是JSON格式");
                return baseResult;
            }
        }

        GroupBoardDTO boardDTO = getGroupBoardDTO(messageDTO.getUserId(), messageDTO.getBoardCode());
        if (boardDTO == null) {
            baseResult.setErrorMessage(ResultCode.RESOURCE_NOT_FOUND.getCode(), "模板不存在");
            return baseResult;
        }
        List<AddressDTO> addresses = boardDTO.getAddresses();
        List<String> reasons = new ArrayList<>();
        for (AddressDTO address : addresses) {
            if (!address.matchCondition(data)) {
                reasons.add(StrFormatter.format("[消息未发送]目标:{},原因:条件不匹配", address.getName()));
                continue;
            }
            // 将模板和数据转换成待发布的消息对象
            Message message = spanner.make(boardDTO, data);
            // 将message发送到指定的address
            PojoResult<DingTalkResponse> pojoResult = sender.send(address, message, DingTalkResponse.class);
            DingTalkResponse response = pojoResult.getContent();
            if (!pojoResult.isSuccess() || response.getErrcode() != 0) {
                reasons.add(StrFormatter.format("[发送消息失败]目标:{},原因:{}({})", address.getName(), response.getErrmsg(), response.getErrcode()));
            }
        }
        if (CollectionUtil.isNotEmpty(reasons)) {
            baseResult.setErrorMessage(ResultCode.BIZ_FAIL.getCode(), String.join("##", reasons));
        }
        return baseResult;
    }


    private GroupBoardDTO getGroupBoardDTO(Long userId, String boardCode) {
        GroupBoardMatchDTO boardMatchDTO = GroupBoardMatchDTO.builder()
                .userId(userId)
                .boardCode(boardCode)
                .build();
        PojoResult<GroupBoardDTO> pojoResult = groupBoardCenter.findGroupBoard(boardMatchDTO);
        return pojoResult.isSuccess() ? pojoResult.getContent() : null;
    }

}
