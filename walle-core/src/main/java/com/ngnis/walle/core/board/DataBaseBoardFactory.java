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
import com.ngnis.walle.datasource.db.board.BoardDO;
import com.ngnis.walle.service.BoardService;
import com.ngnis.walle.web.BoardQueryDTO;
import com.github.pagehelper.PageInfo;
import com.ngnis.walle.common.BeanUtil;
import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.ResultCode;
import com.ngnis.walle.datasource.db.board.BoardDO;
import com.ngnis.walle.web.BoardQueryDTO;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author houyi
 */
public class DataBaseBoardFactory implements BoardFactory {

    private Spanner spanner;

    private BoardService boardService;

    public DataBaseBoardFactory() {
        this.spanner = BeanUtil.getBean(Spanner.class);
        this.boardService = BeanUtil.getBean(BoardService.class);
    }


    /**
     * FIXME: 如果分布式部署，需要加分布式锁
     */
    @Override
    public synchronized BaseResult createBoard(Board board) {
        BaseResult baseResult = spanner.check(board);
        if (!baseResult.isSuccess()) {
            return baseResult;
        }
        Long userId = board.getUserId();
        String boardCode = board.getBoardCode();
        int cnt = boardService.getBoardCnt(userId, boardCode);
        if (cnt > 0) {
            baseResult.setErrorMessage(ResultCode.RECORD_ALREADY_EXISTS.getCode(), StrFormatter.format("模板编号({})已存在", boardCode));
            return baseResult;
        }
        BoardDO boardDO = to(board);
        boardService.saveBoardDO(boardDO);
        return baseResult;
    }

    @Override
    public synchronized BaseResult modifyBoard(Board board) {
        BaseResult baseResult = spanner.check(board);
        if (!baseResult.isSuccess()) {
            return baseResult;
        }
        Long userId = board.getUserId();
        String boardCode = board.getBoardCode();
        BoardDO boardDO = boardService.getBoardDO(userId, boardCode);
        if (boardDO == null) {
            baseResult.setErrorMessage(ResultCode.RESOURCE_NOT_FOUND.getCode(), StrFormatter.format("模板编号({})不存在", boardCode));
            return baseResult;
        }
        // 待修改对象
        boardDO = to(board);
        boardService.updateBoardDO(boardDO);
        return baseResult;
    }

    @Override
    public synchronized BaseResult removeBoard(Board board) {
        BaseResult baseResult = new BaseResult();
        Long userId = board.getUserId();
        String boardCode = board.getBoardCode();
        BoardDO boardDO = boardService.getBoardDO(userId, boardCode);
        if (boardDO == null) {
            baseResult.setErrorMessage(ResultCode.RESOURCE_NOT_FOUND.getCode(), StrFormatter.format("模板编号({})不存在", boardCode));
            return baseResult;
        }
        boardService.deleteBoardDO(userId, boardCode);
        return baseResult;
    }

    @Override
    public Board findBoard(Board board) {
        BoardDO boardDO = boardService.getBoardDO(board.getUserId(), board.getBoardCode());
        return transfer(boardDO);
    }

    @Override
    public PageResult<Board> getBoardPage(BoardQueryDTO queryDTO) {
        // 利用PageHelper进行分页查询，得到的结果是一个Page对象
        List<BoardDO> pageList = boardService.getBoardList(queryDTO);
        // DO分页对象
        PageInfo<BoardDO> pageInfo = new PageInfo<>(pageList);
        List<BoardDO> boardDOList = pageInfo.getList();
        // 创建分页结果
        PageResult<Board> pageResult = new PageResult<>();
        Page<Board> page = new Page<>();
        page.setTotalCount(pageInfo.getTotal());
        page.setCurrentPage(queryDTO.getPageNo());
        page.setPageSize(queryDTO.getPageSize());
        // 结果转换
        List<Board> items = CollectionUtil.isEmpty(boardDOList)
                 ? Collections.emptyList()
                 : boardDOList.stream()
                .map(this::transfer)
                .collect(Collectors.toList());
        page.setItems(items);
        pageResult.setContent(page);
        return pageResult;
    }

    private Board transfer(BoardDO boardDO) {
        if (boardDO == null) {
            return null;
        }
        Board board = BeanMapper.map(boardDO, Board.class);
        board.setAddresses(JSON.parseArray(boardDO.getAddressList(), Address.class));
        return board;
    }

    private BoardDO to(Board board) {
        if (board == null) {
            return null;
        }
        Date now = new Date();
        BoardDO boardDO = BeanMapper.map(board, BoardDO.class);
        boardDO.setAddressList(JSON.toJSONString(board.getAddresses()));
        boardDO.setGmtCreate(now);
        boardDO.setGmtModified(now);
        boardDO.setIsDeleted(Constants.NOT_DELETED);
        return boardDO;
    }

}
