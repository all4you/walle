package com.ngnis.walle.service;

import cn.hutool.core.util.StrUtil;
import com.ngnis.walle.common.Constants;
import com.ngnis.walle.datasource.db.board.BoardDO;
import com.ngnis.walle.datasource.db.board.BoardMapper;
import com.ngnis.walle.web.BoardQueryDTO;
import com.github.pagehelper.PageHelper;
import com.ngnis.walle.common.Constants;
import com.ngnis.walle.datasource.db.board.BoardDO;
import com.ngnis.walle.datasource.db.board.BoardMapper;
import com.ngnis.walle.web.BoardQueryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author houyi
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BoardService {

    private final BoardMapper boardMapper;

    public void saveBoardDO(BoardDO boardDO) {
        boardMapper.insertSelective(boardDO);
    }

    public void updateBoardDO(BoardDO boardDO) {
        Long userId = boardDO.getUserId();
        String boardCode = boardDO.getBoardCode();
        if (userId == null || StrUtil.isBlank(boardCode)) {
            return;
        }
        Example example = new Example(BoardDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", Constants.NOT_DELETED);
        criteria.andEqualTo("userId", boardDO.getUserId());
        criteria.andEqualTo("boardCode", boardDO.getBoardCode());
        boardMapper.updateByExampleSelective(boardDO, example);
    }

    public BoardDO getBoardDO(Long userId, String boardCode) {
        if (userId == null || StrUtil.isBlank(boardCode)) {
            return null;
        }
        Example example = new Example(BoardDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", Constants.NOT_DELETED);
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("boardCode", boardCode);
        return boardMapper.selectOneByExample(example);
    }

    public int getBoardCnt(Long userId, String boardCode) {
        if (userId == null || StrUtil.isBlank(boardCode)) {
            return 0;
        }
        Example example = new Example(BoardDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", Constants.NOT_DELETED);
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("boardCode", boardCode);
        return boardMapper.selectCountByExample(example);
    }

    public void deleteBoardDO(Long userId, String boardCode) {
        if (userId == null || StrUtil.isBlank(boardCode)) {
            return;
        }
        Example example = new Example(BoardDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", Constants.NOT_DELETED);
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("boardCode", boardCode);
        boardMapper.deleteByExample(example);
    }

    public List<BoardDO> getBoardList(BoardQueryDTO queryDTO) {
        // 分页
        PageHelper.startPage(queryDTO.getPageNo(), queryDTO.getPageSize(), "gmt_modified desc");
        // 查询到DO列表
        return boardMapper.selectListByCondition(queryDTO);
    }

}
