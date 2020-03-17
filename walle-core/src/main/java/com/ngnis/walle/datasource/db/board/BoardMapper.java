package com.ngnis.walle.datasource.db.board;

import com.ngnis.walle.datasource.mybatis.Mapper;
import com.ngnis.walle.web.BoardQueryDTO;
import com.ngnis.walle.datasource.mybatis.Mapper;
import com.ngnis.walle.web.BoardQueryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BoardMapper extends Mapper<BoardDO> {

    List<BoardDO> selectListByCondition(@Param("record") BoardQueryDTO query);

}