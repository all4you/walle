package com.ngnis.walle.core.robot;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.ngnis.walle.common.GenericLogUtil;
import com.ngnis.walle.common.HttpContext;
import com.ngnis.walle.common.bean.BeanValidator;
import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.PageResult;
import com.ngnis.walle.common.result.PojoResult;
import com.ngnis.walle.common.result.ResultCode;
import com.ngnis.walle.core.board.Address;
import com.ngnis.walle.core.board.GroupBoard;
import com.ngnis.walle.core.board.GroupBoardFactory;
import com.ngnis.walle.core.board.Spanner;
import com.ngnis.walle.core.message.Message;
import com.ngnis.walle.core.sender.DingTalkResponse;
import com.ngnis.walle.core.sender.Sender;
import com.ngnis.walle.web.GroupBoardQueryDTO;
import com.ngnis.walle.web.SendGroupMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认的Robot实现类
 *
 * @author houyi.wh
 * @since 2018-09-09
 */
@Slf4j
@Service("walleRobot")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WalleRobot implements Robot {

    private final GroupBoardFactory boardFactory;

    private final Spanner spanner;

    private final Sender sender;

    /**
     * 设置当前用户id
     */
    private void fillUserId(GroupBoard board) {
        if (board == null) {
            board = new GroupBoard();
        }
        board.setUserId(HttpContext.currentContext().getUserId());
    }

    private void fillUserId(GroupBoardQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new GroupBoardQueryDTO();
        }
        queryDTO.setUserId(HttpContext.currentContext().getUserId());
    }

    private GroupBoard newBoard(String boardCode) {
        Long userId = HttpContext.currentContext().getUserId();
        return GroupBoard.builder()
                .userId(userId)
                .boardCode(boardCode)
                .build();
    }

    @Override
    public BaseResult createGroupBoard(GroupBoard board) {
        fillUserId(board);
        log.info("createGroupBoard with board={}", board);
        return boardFactory.createGroupBoard(board);
    }

    @Override
    public BaseResult modifyGroupBoard(GroupBoard board) {
        fillUserId(board);
        log.info("modifyGroupBoard with board={}", board);
        return boardFactory.modifyGroupBoard(board);
    }

    @Override
    public BaseResult removeGroupBoard(String boardCode) {
        GroupBoard board = newBoard(boardCode);
        log.info("removeGroupBoard with board={}", board);
        return boardFactory.removeGroupBoard(board);
    }

    @Override
    public PageResult<GroupBoard> getGroupBoardPage(GroupBoardQueryDTO queryDTO) {
        fillUserId(queryDTO);
        return boardFactory.getGroupBoardPage(queryDTO);
    }

    @Override
    public PojoResult<GroupBoard> findGroupBoard(String boardCode) {
        GroupBoard query = newBoard(boardCode);
        log.info("findGroupBoard with query={}", query);
        PojoResult<GroupBoard> pojoResult = new PojoResult<>();
        GroupBoard board = boardFactory.findGroupBoard(query);
        if (board == null) {
            pojoResult.setErrorMessage(ResultCode.RESOURCE_NOT_FOUND.getCode(), "模板不存在");
        } else {
            pojoResult.setContent(board);
        }
        return pojoResult;
    }

    @Override
    public BaseResult sendGroupMessage(SendGroupMessageDTO dto) {
        BaseResult baseResult = BeanValidator.validate(dto);
        if (!baseResult.isSuccess()) {
            return baseResult;
        }
        Map<String, Object> data = new HashMap<>();
        if (StrUtil.isNotBlank(dto.getData())) {
            try {
                data = JSON.parseObject(dto.getData(), Map.class);
            } catch (Exception e) {
                baseResult.setErrorMessage(ResultCode.PARAM_INVALID.getCode(), "携带的数据只能是JSON格式");
                return baseResult;
            }
        }
        GroupBoard query = newBoard(dto.getBoardCode());
        GroupBoard board = boardFactory.findGroupBoard(query);
        if (board == null) {
            baseResult.setErrorMessage(ResultCode.RESOURCE_NOT_FOUND.getCode(), "模板不存在");
            return baseResult;
        }
        List<Address> addresses = board.getAddresses();
        List<String> reasons = new ArrayList<>();
        for (Address address : addresses) {
            if (!address.matchCondition(data)) {
                reasons.add(StrFormatter.format("[消息未发送]目标:{},原因:条件不匹配", address.getName()));
                continue;
            }
            // 将模板和数据转换成待发布的消息对象
            Message message = spanner.make(board, data);
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
        GenericLogUtil.invokeSuccess(log, "sendGroupMessage", StrFormatter.format("dto={}", JSON.toJSONString(dto)), StrFormatter.format("baseResult={}", JSON.toJSONString(baseResult)));
        return baseResult;
    }
}
