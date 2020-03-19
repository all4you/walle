package com.ngnis.walle.core.robot;

import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.PageResult;
import com.ngnis.walle.common.result.PojoResult;
import com.ngnis.walle.core.board.Board;
import com.ngnis.walle.web.BoardQueryDTO;
import com.ngnis.walle.web.SendMessageDTO;

/**
 * 对外提供的所有服务都通过这个接口来提供
 *
 * @author houyi.wh
 * @since 2018-09-09
 */
public interface Robot {

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
     * @param boardCode 模板编码
     * @return 是否成功
     */
    BaseResult removeBoard(String boardCode);

    /**
     * 根据查询条件获取分页的结果
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageResult<Board> getBoardPage(BoardQueryDTO queryDTO);

    /**
     * 获取模板详情
     *
     * @param boardCode 模板编码
     */
    PojoResult<Board> findBoard(String boardCode);

    /**
     * 发送消息
     *
     * @param dto 发送消息对象
     * @return 返回的结果
     */
    BaseResult sendMessage(SendMessageDTO dto);

}
