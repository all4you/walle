package com.ngnis.walle.service.board;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.ngnis.walle.center.board.*;
import com.ngnis.walle.common.BeanUtil;
import com.ngnis.walle.common.Constants;
import com.ngnis.walle.common.bean.BeanMapper;
import com.ngnis.walle.common.bean.BeanValidator;
import com.ngnis.walle.common.log.GenericLogUtil;
import com.ngnis.walle.common.log.PrintLog;
import com.ngnis.walle.common.result.*;
import com.ngnis.walle.core.message.Message;
import com.ngnis.walle.core.sender.DingTalkResponse;
import com.ngnis.walle.core.sender.Sender;
import com.ngnis.walle.datasource.db.board.GroupBoardDO;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author houyi
 */
@Slf4j
public class DefaultGroupBoardCenter implements GroupBoardCenter {

    private Spanner spanner;

    private GroupBoardService boardService;

    private Sender sender;

    public DefaultGroupBoardCenter() {
        this.spanner = BeanUtil.getBean(Spanner.class);
        this.boardService = BeanUtil.getBean(GroupBoardService.class);
        this.sender = BeanUtil.getBean(Sender.class);
    }


    /**
     * FIXME: 如果分布式部署，需要加分布式锁
     */
    @PrintLog
    @Override
    public synchronized BaseResult createGroupBoard(GroupBoardDTO boardDTO) {
        BaseResult baseResult = spanner.check(boardDTO);
        if (!baseResult.isSuccess()) {
            return baseResult;
        }
        Long userId = boardDTO.getUserId();
        String boardCode = boardDTO.getBoardCode();
        int cnt = boardService.getGroupBoardCnt(userId, boardCode);
        if (cnt > 0) {
            baseResult.setErrorMessage(ResultCode.RECORD_ALREADY_EXISTS.getCode(), StrFormatter.format("模板编号({})已存在", boardCode));
            return baseResult;
        }
        GroupBoardDO boardDO = to(boardDTO);
        boardService.saveGroupBoardDO(boardDO);
        return baseResult;
    }

    @PrintLog
    @Override
    public synchronized BaseResult modifyGroupBoard(GroupBoardDTO boardDTO) {
        BaseResult baseResult = spanner.check(boardDTO);
        if (!baseResult.isSuccess()) {
            return baseResult;
        }
        Long userId = boardDTO.getUserId();
        String boardCode = boardDTO.getBoardCode();
        GroupBoardDO boardDO = boardService.getGroupBoardDO(userId, boardCode);
        if (boardDO == null) {
            baseResult.setErrorMessage(ResultCode.RESOURCE_NOT_FOUND.getCode(), StrFormatter.format("模板编号({})不存在", boardCode));
            return baseResult;
        }
        // 待修改对象
        boardDO = to(boardDTO);
        boardService.updateGroupBoardDO(boardDO);
        return baseResult;
    }

    @PrintLog
    @Override
    public synchronized BaseResult removeGroupBoard(GroupBoardDTO boardDTO) {
        BaseResult baseResult = new BaseResult();
        Long userId = boardDTO.getUserId();
        String boardCode = boardDTO.getBoardCode();
        GroupBoardDO boardDO = boardService.getGroupBoardDO(userId, boardCode);
        if (boardDO == null) {
            baseResult.setErrorMessage(ResultCode.RESOURCE_NOT_FOUND.getCode(), StrFormatter.format("模板编号({})不存在", boardCode));
            return baseResult;
        }
        boardService.deleteGroupBoardDO(userId, boardCode);
        return baseResult;
    }

    @PrintLog
    @Override
    public PojoResult<GroupBoardDTO> findGroupBoard(GroupBoardDTO boardDTO) {
        PojoResult<GroupBoardDTO> pojoResult = new PojoResult<>();
        GroupBoardDO boardDO = boardService.getGroupBoardDO(boardDTO.getUserId(), boardDTO.getBoardCode());
        if (boardDO == null) {
            pojoResult.setErrorMessage(ResultCode.RESOURCE_NOT_FOUND.getCode(), "模板不存在");
        } else {
            pojoResult.setContent(transfer(boardDO));
        }
        return pojoResult;
    }

    @PrintLog
    @Override
    public PageResult<GroupBoardDTO> getGroupBoardPage(GroupBoardQueryDTO queryDTO) {
        // 利用PageHelper进行分页查询，得到的结果是一个Page对象
        List<GroupBoardDO> pageList = boardService.getGroupBoardList(queryDTO);
        // DO分页对象
        PageInfo<GroupBoardDO> pageInfo = new PageInfo<>(pageList);
        List<GroupBoardDO> boardDOList = pageInfo.getList();
        // 创建分页结果
        PageResult<GroupBoardDTO> pageResult = new PageResult<>();
        Page<GroupBoardDTO> page = new Page<>();
        page.setTotalCount(pageInfo.getTotal());
        page.setCurrentPage(queryDTO.getPageNo());
        page.setPageSize(queryDTO.getPageSize());
        // 结果转换
        List<GroupBoardDTO> items = CollectionUtil.isEmpty(boardDOList)
                ? Collections.emptyList()
                : boardDOList.stream()
                .map(this::transfer)
                .collect(Collectors.toList());
        page.setItems(items);
        pageResult.setContent(page);
        return pageResult;
    }

    @PrintLog
    @Override
    public PojoResult<Integer> getGroupBoardCnt(GroupBoardQueryDTO queryDTO) {
        PojoResult<Integer> pojoResult = new PojoResult<>();
        int cnt = boardService.getGroupBoardCnt(queryDTO.getUserId(), null);
        pojoResult.setContent(cnt);
        return pojoResult;
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
        GroupBoardDO boardDO = boardService.getGroupBoardDO(messageDTO.getUserId(), messageDTO.getBoardCode());
        if (boardDO == null) {
            baseResult.setErrorMessage(ResultCode.RESOURCE_NOT_FOUND.getCode(), "模板不存在");
            return baseResult;
        }
        GroupBoardDTO boardDTO = transfer(boardDO);
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
        GenericLogUtil.invokeSuccess(log, "sendGroupMessage", StrFormatter.format("dto={}", JSON.toJSONString(messageDTO)), StrFormatter.format("baseResult={}", JSON.toJSONString(baseResult)));
        return baseResult;
    }

    private GroupBoardDTO transfer(GroupBoardDO boardDO) {
        if (boardDO == null) {
            return null;
        }
        GroupBoardDTO board = BeanMapper.map(boardDO, GroupBoardDTO.class);
        board.setAddresses(JSON.parseArray(boardDO.getAddressList(), AddressDTO.class));
        return board;
    }

    private GroupBoardDO to(GroupBoardDTO board) {
        if (board == null) {
            return null;
        }
        Date now = new Date();
        GroupBoardDO boardDO = BeanMapper.map(board, GroupBoardDO.class);
        boardDO.setAddressList(JSON.toJSONString(board.getAddresses()));
        boardDO.setGmtCreate(now);
        boardDO.setGmtModified(now);
        boardDO.setIsDeleted(Constants.NOT_DELETED);
        return boardDO;
    }

}
