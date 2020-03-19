package com.ngnis.walle.web;

import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.PageResult;
import com.ngnis.walle.common.result.PojoResult;
import com.ngnis.walle.core.auth.CheckToken;
import com.ngnis.walle.core.board.GroupBoard;
import com.ngnis.walle.core.robot.Robot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 模板增删改相关接口
 *
 * @author houyi.wh
 * @since 2018-09-14
 */
@Slf4j
@RestController
public class GroupBoardController {

    @Resource
    @Qualifier("walleRobot")
    private Robot robot;

    /**
     * 查询Board列表
     */
    @CheckToken
    @GetMapping(ApiConstant.Urls.GET_BOARDS)
    public PageResult<GroupBoard> getGroupBoards(GroupBoardQueryDTO queryDTO) {
        return robot.getGroupBoardPage(queryDTO);
    }

    /**
     * 添加Board
     * RequestBody用来接收Post请求体中的数据
     */
    @CheckToken
    @PostMapping(ApiConstant.Urls.CREATE_BOARD)
    public BaseResult createGroupBoard(@RequestBody GroupBoard board) {
        return robot.createGroupBoard(board);
    }

    /**
     * 更新Board
     */
    @CheckToken
    @PostMapping(ApiConstant.Urls.MODIFY_BOARD)
    public BaseResult modifyGroupBoard(@RequestBody GroupBoard board) {
        return robot.modifyGroupBoard(board);
    }

    /**
     * 删除Board
     */
    @CheckToken
    @PostMapping(ApiConstant.Urls.REMOVE_BOARD)
    public BaseResult removeGroupBoard(@RequestParam String boardCode) {
        return robot.removeGroupBoard(boardCode);
    }

    /**
     * 查询Board列表
     */
    @CheckToken
    @GetMapping(ApiConstant.Urls.FIND_BOARD)
    public PojoResult<GroupBoard> findGroupBoard(@PathVariable String boardCode) {
        return robot.findGroupBoard(boardCode);
    }

}
