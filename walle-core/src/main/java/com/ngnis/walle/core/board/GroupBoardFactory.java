package com.ngnis.walle.core.board;

import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.PageResult;
import com.ngnis.walle.common.result.PojoResult;
import com.ngnis.walle.web.GroupBoardQueryDTO;

/**
 * 群消息模板工厂
 *
 * @author houyi
 */
public interface GroupBoardFactory {

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
     * @param board 模板编码
     * @return 是否成功
     */
    BaseResult removeGroupBoard(GroupBoard board);

    /**
     * 查询模板
     *
     * @param board 模板编码
     * @return 模板
     */
    GroupBoard findGroupBoard(GroupBoard board);

    /**
     * 根据查询条件获取分页的结果
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageResult<GroupBoard> getGroupBoardPage(GroupBoardQueryDTO queryDTO);

    /**
     * 获取模板个数
     */
    PojoResult<Integer> getGroupBoardCnt(Long userId);

}
