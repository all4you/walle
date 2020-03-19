package com.ngnis.walle.datasource.db.board;

import com.ngnis.walle.datasource.mybatis.Mapper;
import com.ngnis.walle.web.GroupBoardQueryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GroupBoardMapper extends Mapper<GroupBoardDO> {

    List<GroupBoardDO> selectListByCondition(@Param("record") GroupBoardQueryDTO query);

}