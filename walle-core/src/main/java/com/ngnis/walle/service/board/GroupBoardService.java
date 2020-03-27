package com.ngnis.walle.service.board;

import cn.hutool.core.util.StrUtil;
import com.ngnis.walle.common.Constants;
import com.ngnis.walle.datasource.db.board.GroupBoardDO;
import com.ngnis.walle.datasource.db.board.GroupBoardMapper;
import com.ngnis.walle.center.board.GroupBoardQueryDTO;
import com.github.pagehelper.PageHelper;
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
public class GroupBoardService {

    private final GroupBoardMapper groupBoardMapper;

    public void saveGroupBoardDO(GroupBoardDO boardDO) {
        groupBoardMapper.insertSelective(boardDO);
    }

    public void updateGroupBoardDO(GroupBoardDO boardDO) {
        Long userId = boardDO.getUserId();
        String boardCode = boardDO.getBoardCode();
        if (userId == null || StrUtil.isBlank(boardCode)) {
            return;
        }
        Example example = new Example(GroupBoardDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", Constants.NOT_DELETED);
        criteria.andEqualTo("userId", boardDO.getUserId());
        criteria.andEqualTo("boardCode", boardDO.getBoardCode());
        groupBoardMapper.updateByExampleSelective(boardDO, example);
    }

    public GroupBoardDO getGroupBoardDO(Long userId, String boardCode) {
        if (userId == null || StrUtil.isBlank(boardCode)) {
            return null;
        }
        Example example = new Example(GroupBoardDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", Constants.NOT_DELETED);
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("boardCode", boardCode);
        return groupBoardMapper.selectOneByExample(example);
    }

    public int getGroupBoardCnt(Long userId, String boardCode) {
        if (userId == null) {
            return 0;
        }
        Example example = new Example(GroupBoardDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", Constants.NOT_DELETED);
        criteria.andEqualTo("userId", userId);
        if (StrUtil.isNotBlank(boardCode)) {
            criteria.andEqualTo("boardCode", boardCode);
        }
        return groupBoardMapper.selectCountByExample(example);
    }

    public void deleteGroupBoardDO(Long userId, String boardCode) {
        if (userId == null || StrUtil.isBlank(boardCode)) {
            return;
        }
        Example example = new Example(GroupBoardDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", Constants.NOT_DELETED);
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("boardCode", boardCode);
        groupBoardMapper.deleteByExample(example);
    }

    public List<GroupBoardDO> getGroupBoardList(GroupBoardQueryDTO queryDTO) {
        // 分页
        PageHelper.startPage(queryDTO.getPageNo(), queryDTO.getPageSize(), "gmt_modified desc");
        // 查询到DO列表
        return groupBoardMapper.selectListByCondition(queryDTO);
    }

}
