package com.ngnis.walle.core.board;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.ngnis.walle.common.BeanUtil;
import com.ngnis.walle.common.result.*;
import com.ngnis.walle.web.GroupBoardQueryDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author houyi
 */
@Slf4j
public class MemoryBoardFactory implements GroupBoardFactory {

    private Map<String, GroupBoard> boardMap;

    private Spanner spanner;

    public MemoryBoardFactory() {
        this.boardMap = new ConcurrentHashMap<>();
        this.spanner = BeanUtil.getBean(Spanner.class);
    }

    @Override
    public synchronized BaseResult createGroupBoard(GroupBoard board) {
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
    public synchronized BaseResult modifyGroupBoard(GroupBoard board) {
        BaseResult baseResult = spanner.check(board);
        if (!baseResult.isSuccess()) {
            return baseResult;
        }
        String boardCode = board.getBoardCode();
        GroupBoard existsBoard = findGroupBoard(board);
        if (existsBoard == null) {
            baseResult.setErrorMessage(ResultCode.RESOURCE_NOT_FOUND.getCode(), StrFormatter.format("模板编号({})不存在", boardCode));
            return baseResult;
        }
        boardMap.put(boardCode, board);
        return baseResult;
    }

    @Override
    public synchronized BaseResult removeGroupBoard(GroupBoard board) {
        BaseResult baseResult = new BaseResult();
        GroupBoard existsBoard = findGroupBoard(board);
        String boardCode = board.getBoardCode();
        if (existsBoard == null) {
            baseResult.setErrorMessage(ResultCode.RESOURCE_NOT_FOUND.getCode(), StrFormatter.format("模板编号({})不存在", boardCode));
            return baseResult;
        }
        boardMap.remove(boardCode);
        return baseResult;
    }

    @Override
    public GroupBoard findGroupBoard(GroupBoard board) {
        String boardCode = board.getBoardCode();
        if (StrUtil.isBlank(boardCode)) {
            return null;
        }
        return boardMap.get(boardCode);
    }

    @Override
    public PageResult<GroupBoard> getGroupBoardPage(GroupBoardQueryDTO queryDTO) {
        int pageNo = queryDTO.getPageNo();
        int pageSize = queryDTO.getPageSize();
        List<GroupBoard> boards = CollectionUtil.newArrayList(boardMap.values());
        String query = queryDTO.getQuery();
        if (StrUtil.isNotBlank(query)) {
            boards = boards.stream()
                    .filter(item -> (item.getBoardCode().contains(query) || item.getBoardName().contains(query)))
                    .collect(Collectors.toList());
        }
        int totalCount = boards.size();
        PageResult<GroupBoard> pageResult = new PageResult<>();
        Page<GroupBoard> page = new Page<>();
        page.setTotalCount((long) totalCount);
        page.setCurrentPage(pageNo);
        page.setPageSize(pageSize);
        if (totalCount > 0) {
            int fromIndex = (pageNo - 1) * pageSize;
            int toIndex = pageNo * pageSize;
            if (toIndex > totalCount) {
                toIndex = totalCount;
            }
            List<GroupBoard> pageList = boards.subList(fromIndex, toIndex);
            page.setItems(pageList);
        }
        pageResult.setContent(page);
        return pageResult;
    }

    @Override
    public PojoResult<Integer> getGroupBoardCnt(Long userId) {
        int cnt = boardMap.values().size();
        PojoResult<Integer> pojoResult = new PojoResult<>();
        pojoResult.setContent(cnt);
        return pojoResult;
    }

}
