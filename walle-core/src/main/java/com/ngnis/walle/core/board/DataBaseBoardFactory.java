package com.ngnis.walle.core.board;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.text.StrFormatter;
import com.alibaba.fastjson.JSON;
import com.ngnis.walle.common.BeanUtil;
import com.ngnis.walle.common.Constants;
import com.ngnis.walle.common.bean.BeanMapper;
import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.Page;
import com.ngnis.walle.common.result.PageResult;
import com.ngnis.walle.common.result.ResultCode;
import com.ngnis.walle.datasource.db.board.GroupBoardDO;
import com.ngnis.walle.service.GroupBoardService;
import com.ngnis.walle.web.GroupBoardQueryDTO;
import com.github.pagehelper.PageInfo;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author houyi
 */
public class DataBaseBoardFactory implements GroupBoardFactory {

    private Spanner spanner;

    private GroupBoardService boardService;

    public DataBaseBoardFactory() {
        this.spanner = BeanUtil.getBean(Spanner.class);
        this.boardService = BeanUtil.getBean(GroupBoardService.class);
    }


    /**
     * FIXME: 如果分布式部署，需要加分布式锁
     */
    @Override
    public synchronized BaseResult createGroupBoard(GroupBoard board) {
        BaseResult baseResult = spanner.check(board);
        if (!baseResult.isSuccess()) {
            return baseResult;
        }
        Long userId = board.getUserId();
        String boardCode = board.getBoardCode();
        int cnt = boardService.getGroupBoardCnt(userId, boardCode);
        if (cnt > 0) {
            baseResult.setErrorMessage(ResultCode.RECORD_ALREADY_EXISTS.getCode(), StrFormatter.format("模板编号({})已存在", boardCode));
            return baseResult;
        }
        GroupBoardDO boardDO = to(board);
        boardService.saveGroupBoardDO(boardDO);
        return baseResult;
    }

    @Override
    public synchronized BaseResult modifyGroupBoard(GroupBoard board) {
        BaseResult baseResult = spanner.check(board);
        if (!baseResult.isSuccess()) {
            return baseResult;
        }
        Long userId = board.getUserId();
        String boardCode = board.getBoardCode();
        GroupBoardDO boardDO = boardService.getGroupBoardDO(userId, boardCode);
        if (boardDO == null) {
            baseResult.setErrorMessage(ResultCode.RESOURCE_NOT_FOUND.getCode(), StrFormatter.format("模板编号({})不存在", boardCode));
            return baseResult;
        }
        // 待修改对象
        boardDO = to(board);
        boardService.updateGroupBoardDO(boardDO);
        return baseResult;
    }

    @Override
    public synchronized BaseResult removeGroupBoard(GroupBoard board) {
        BaseResult baseResult = new BaseResult();
        Long userId = board.getUserId();
        String boardCode = board.getBoardCode();
        GroupBoardDO boardDO = boardService.getGroupBoardDO(userId, boardCode);
        if (boardDO == null) {
            baseResult.setErrorMessage(ResultCode.RESOURCE_NOT_FOUND.getCode(), StrFormatter.format("模板编号({})不存在", boardCode));
            return baseResult;
        }
        boardService.deleteGroupBoardDO(userId, boardCode);
        return baseResult;
    }

    @Override
    public GroupBoard findGroupBoard(GroupBoard board) {
        GroupBoardDO boardDO = boardService.getGroupBoardDO(board.getUserId(), board.getBoardCode());
        return transfer(boardDO);
    }

    @Override
    public PageResult<GroupBoard> getGroupBoardPage(GroupBoardQueryDTO queryDTO) {
        // 利用PageHelper进行分页查询，得到的结果是一个Page对象
        List<GroupBoardDO> pageList = boardService.getGroupBoardList(queryDTO);
        // DO分页对象
        PageInfo<GroupBoardDO> pageInfo = new PageInfo<>(pageList);
        List<GroupBoardDO> boardDOList = pageInfo.getList();
        // 创建分页结果
        PageResult<GroupBoard> pageResult = new PageResult<>();
        Page<GroupBoard> page = new Page<>();
        page.setTotalCount(pageInfo.getTotal());
        page.setCurrentPage(queryDTO.getPageNo());
        page.setPageSize(queryDTO.getPageSize());
        // 结果转换
        List<GroupBoard> items = CollectionUtil.isEmpty(boardDOList)
                 ? Collections.emptyList()
                 : boardDOList.stream()
                .map(this::transfer)
                .collect(Collectors.toList());
        page.setItems(items);
        pageResult.setContent(page);
        return pageResult;
    }

    private GroupBoard transfer(GroupBoardDO boardDO) {
        if (boardDO == null) {
            return null;
        }
        GroupBoard board = BeanMapper.map(boardDO, GroupBoard.class);
        board.setAddresses(JSON.parseArray(boardDO.getAddressList(), Address.class));
        return board;
    }

    private GroupBoardDO to(GroupBoard board) {
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
