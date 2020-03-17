package com.ngnis.walle.core.board;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.ngnis.walle.common.BeanUtil;
import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.Page;
import com.ngnis.walle.common.result.PageResult;
import com.ngnis.walle.common.result.ResultCode;
import com.ngnis.walle.web.BoardQueryDTO;
import com.ngnis.walle.common.BeanUtil;
import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.ResultCode;
import com.ngnis.walle.web.BoardQueryDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author houyi
 */
@Slf4j
public class MemoryBoardFactory implements BoardFactory {

    private Map<String, Board> boardMap;

    private Spanner spanner;

    public MemoryBoardFactory() {
        this.boardMap = new ConcurrentHashMap<>();
        this.spanner = BeanUtil.getBean(Spanner.class);
    }

    @Override
    public synchronized BaseResult createBoard(Board board) {
        BaseResult baseResult = spanner.check(board);
        if (!baseResult.isSuccess()) {
            return baseResult;
        }
        String boardCode = board.getBoardCode();
        board.setBoardId(IdUtil.objectId());
        // 如果map中不存在，返回的对象是null，才能put成功
        boolean success = boardMap.putIfAbsent(boardCode, board) == null;
        if (!success) {
            baseResult.setErrorMessage(ResultCode.RECORD_ALREADY_EXISTS.getCode(), StrFormatter.format("模板编号({})已存在", boardCode));
            return baseResult;
        }
        return baseResult;
    }

    @Override
    public synchronized BaseResult modifyBoard(Board board) {
        BaseResult baseResult = spanner.check(board);
        if (!baseResult.isSuccess()) {
            return baseResult;
        }
        String boardCode = board.getBoardCode();
        Board existsBoard = findBoard(board);
        if (existsBoard == null) {
            baseResult.setErrorMessage(ResultCode.RESOURCE_NOT_FOUND.getCode(), StrFormatter.format("模板编号({})不存在", boardCode));
            return baseResult;
        }
        boardMap.put(boardCode, board);
        return baseResult;
    }

    @Override
    public synchronized BaseResult removeBoard(Board board) {
        BaseResult baseResult = new BaseResult();
        Board existsBoard = findBoard(board);
        String boardCode = board.getBoardCode();
        if (existsBoard == null) {
            baseResult.setErrorMessage(ResultCode.RESOURCE_NOT_FOUND.getCode(), StrFormatter.format("模板编号({})不存在", boardCode));
            return baseResult;
        }
        boardMap.remove(boardCode);
        return baseResult;
    }

    @Override
    public Board findBoard(Board board) {
        String boardCode = board.getBoardCode();
        if (StrUtil.isBlank(boardCode)) {
            return null;
        }
        return boardMap.get(boardCode);
    }

    @Override
    public PageResult<Board> getBoardPage(BoardQueryDTO queryDTO) {
        int pageNo = queryDTO.getPageNo();
        int pageSize = queryDTO.getPageSize();
        List<Board> boards = CollectionUtil.newArrayList(boardMap.values());
        String query = queryDTO.getQuery();
        if (StrUtil.isNotBlank(query)) {
            boards = boards.stream()
                    .filter(item -> (item.getBoardCode().contains(query) || item.getBoardName().contains(query)))
                    .collect(Collectors.toList());
        }
        int totalCount = boards.size();
        PageResult<Board> pageResult = new PageResult<>();
        Page<Board> page = new Page<>();
        page.setTotalCount((long) totalCount);
        page.setCurrentPage(pageNo);
        page.setPageSize(pageSize);
        if (totalCount > 0) {
            int fromIndex = (pageNo - 1) * pageSize;
            int toIndex = pageNo * pageSize;
            if (toIndex > totalCount) {
                toIndex = totalCount;
            }
            List<Board> pageList = boards.subList(fromIndex, toIndex);
            page.setItems(pageList);
        }
        pageResult.setContent(page);
        return pageResult;
    }

}
