package com.ngnis.walle.core.board;

import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.PageResult;
import com.ngnis.walle.web.BoardQueryDTO;
import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.PageResult;
import com.ngnis.walle.web.BoardQueryDTO;

/**
 * 模板工厂
 *
 * @author houyi
 */
public interface BoardFactory {

    /**
     * 创建模板
     *
     * @param board 模板
     * @return 是否成功
     */
    BaseResult createBoard(Board board);

    /**
     * 修改模板
     *
     * @param board 模板
     * @return 是否成功
     */
    BaseResult modifyBoard(Board board);

    /**
     * 删除模板
     *
     * @param board 模板编码
     * @return 是否成功
     */
    BaseResult removeBoard(Board board);

    /**
     * 查询模板
     *
     * @param board 模板编码
     * @return 模板
     */
    Board findBoard(Board board);

    /**
     * 根据查询条件获取分页的结果
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageResult<Board> getBoardPage(BoardQueryDTO queryDTO);

}
