package com.ngnis.walle.web;

import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.PageResult;
import com.ngnis.walle.common.result.PojoResult;
import com.ngnis.walle.core.auth.CheckToken;
import com.ngnis.walle.core.board.Board;
import com.ngnis.walle.core.robot.Robot;
import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.PojoResult;
import com.ngnis.walle.core.board.Board;
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
public class BoardController {

    @Resource
    @Qualifier("walleRobot")
    private Robot robot;

    /**
     * 查询Board列表
     */
    @CheckToken
    @GetMapping(ApiConstant.Urls.GET_BOARDS)
    public PageResult<Board> getBoards(BoardQueryDTO queryDTO) {
        return robot.getBoardPage(queryDTO);
    }

    /**
     * 添加Board
     * RequestBody用来接收Post请求体中的数据
     */
    @CheckToken
    @PostMapping(ApiConstant.Urls.CREATE_BOARD)
    public BaseResult createBoard(@RequestBody Board board) {
        return robot.createBoard(board);
    }

    /**
     * 更新Board
     */
    @CheckToken
    @PostMapping(ApiConstant.Urls.MODIFY_BOARD)
    public BaseResult modifyBoard(@RequestBody Board board) {
        return robot.modifyBoard(board);
    }

    /**
     * 删除Board
     */
    @CheckToken
    @PostMapping(ApiConstant.Urls.REMOVE_BOARD)
    public BaseResult removeBoard(@RequestParam String boardCode) {
        return robot.removeBoard(boardCode);
    }

    /**
     * 查询Board列表
     */
    @CheckToken
    @GetMapping(ApiConstant.Urls.FIND_BOARD)
    public PojoResult<Board> findBoard(@PathVariable String boardCode) {
        return robot.findBoard(boardCode);
    }

}
