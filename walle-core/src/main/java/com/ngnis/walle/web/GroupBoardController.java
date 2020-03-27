package com.ngnis.walle.web;

import com.ngnis.walle.center.board.GroupBoardCenter;
import com.ngnis.walle.center.board.GroupBoardDTO;
import com.ngnis.walle.center.board.GroupBoardQueryDTO;
import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.PageResult;
import com.ngnis.walle.common.result.PojoResult;
import com.ngnis.walle.core.auth.CheckToken;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping(ApiConstant.Urls.GROUP_BOARD)
public class GroupBoardController extends BaseController {

    @Resource
    private GroupBoardCenter groupBoardCenter;

    /**
     * 查询Board列表
     */
    @CheckToken
    @GetMapping(ApiConstant.Urls.GET_BOARDS)
    public PageResult<GroupBoardDTO> getGroupBoards(GroupBoardQueryDTO queryDTO) {
        GroupBoardQueryDTO newQueryDTO = newBoardQueryDTO(queryDTO);
        return groupBoardCenter.getGroupBoardPage(newQueryDTO);
    }

    /**
     * 查询Board个数
     */
    @CheckToken
    @GetMapping(ApiConstant.Urls.GET_BOARDS_CNT)
    public PojoResult<Integer> getGroupBoardCnt(GroupBoardQueryDTO queryDTO) {
        GroupBoardQueryDTO newQueryDTO = newBoardQueryDTO(queryDTO);
        return groupBoardCenter.getGroupBoardCnt(newQueryDTO);
    }

    /**
     * 添加Board
     * RequestBody用来接收Post请求体中的数据
     */
    @CheckToken
    @PostMapping(ApiConstant.Urls.CREATE_BOARD)
    public BaseResult createGroupBoard(@RequestBody GroupBoardDTO boardDTO) {
        GroupBoardDTO newBoardDTO = newBoardDTO(boardDTO);
        return groupBoardCenter.createGroupBoard(newBoardDTO);
    }

    /**
     * 更新Board
     */
    @CheckToken
    @PostMapping(ApiConstant.Urls.MODIFY_BOARD)
    public BaseResult modifyGroupBoard(@RequestBody GroupBoardDTO boardDTO) {
        GroupBoardDTO newBoardDTO = newBoardDTO(boardDTO);
        return groupBoardCenter.modifyGroupBoard(newBoardDTO);
    }

    /**
     * 删除Board
     */
    @CheckToken
    @PostMapping(ApiConstant.Urls.REMOVE_BOARD)
    public BaseResult removeGroupBoard(@RequestParam String boardCode) {
        GroupBoardDTO boardDTO = newBoardDTO(boardCode);
        return groupBoardCenter.removeGroupBoard(boardDTO);
    }

    /**
     * 查询Board列表
     */
    @CheckToken
    @GetMapping(ApiConstant.Urls.FIND_BOARD)
    public PojoResult<GroupBoardDTO> findGroupBoard(@PathVariable String boardCode) {
        GroupBoardDTO boardDTO = newBoardDTO(boardCode);
        return groupBoardCenter.findGroupBoard(boardDTO);
    }

}
