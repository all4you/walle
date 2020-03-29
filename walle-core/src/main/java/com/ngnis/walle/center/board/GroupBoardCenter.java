package com.ngnis.walle.center.board;

import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.PageResult;
import com.ngnis.walle.common.result.PojoResult;

/**
 * 群消息模板工厂
 *
 * @author houyi
 */
public interface GroupBoardCenter {

    /**
     * 创建模板
     *
     * @param boardDTO 模板
     * @return 是否成功
     */
    BaseResult createGroupBoard(GroupBoardDTO boardDTO);

    /**
     * 修改模板
     *
     * @param boardDTO 模板
     * @return 是否成功
     */
    BaseResult modifyGroupBoard(GroupBoardDTO boardDTO);

    /**
     * 删除模板
     *
     * @param boardMatchDTO 模板匹配对象
     * @return 是否成功
     */
    BaseResult removeGroupBoard(GroupBoardMatchDTO boardMatchDTO);

    /**
     * 查询模板
     *
     * @param boardMatchDTO 模板匹配对象
     * @return 模板
     */
    PojoResult<GroupBoardDTO> findGroupBoard(GroupBoardMatchDTO boardMatchDTO);

    /**
     * 根据查询条件获取分页的结果
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageResult<GroupBoardDTO> getGroupBoardPage(GroupBoardQueryDTO queryDTO);

    /**
     * 获取模板个数
     */
    PojoResult<Integer> getGroupBoardCnt(GroupBoardQueryDTO queryDTO);


}
