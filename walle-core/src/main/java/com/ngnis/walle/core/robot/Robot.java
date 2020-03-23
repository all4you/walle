package com.ngnis.walle.core.robot;

import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.PageResult;
import com.ngnis.walle.common.result.PojoResult;
import com.ngnis.walle.core.board.GroupBoard;
import com.ngnis.walle.web.GroupBoardQueryDTO;
import com.ngnis.walle.web.SendGroupMessageDTO;

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
    BaseResult createGroupBoard(GroupBoard board);

    /**
     * 修改模板
     *
     * @param board 模板
     * @return 是否成功
     */
    BaseResult modifyGroupBoard(GroupBoard board);

    /**
     * 删除模板
     *
     * @param boardCode 模板编码
     * @return 是否成功
     */
    BaseResult removeGroupBoard(String boardCode);

    /**
     * 根据查询条件获取分页的结果
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageResult<GroupBoard> getGroupBoardPage(GroupBoardQueryDTO queryDTO);

    /**
     * 获取模板详情
     *
     * @param boardCode 模板编码
     */
    PojoResult<GroupBoard> findGroupBoard(String boardCode);

    /**
     * 获取群消息模板个数
     */
    PojoResult<Integer> getGroupBoardCnt();

    /**
     * 发送群消息
     *
     * @param dto 发送消息对象
     * @return 返回的结果
     */
    BaseResult sendGroupMessage(SendGroupMessageDTO dto);

}
