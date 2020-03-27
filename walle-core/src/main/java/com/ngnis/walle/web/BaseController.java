package com.ngnis.walle.web;

import com.ngnis.walle.center.account.LogoutDTO;
import com.ngnis.walle.center.account.QueryDTO;
import com.ngnis.walle.center.account.UpdatePwdDTO;
import com.ngnis.walle.center.board.GroupBoardDTO;
import com.ngnis.walle.center.board.GroupBoardQueryDTO;
import com.ngnis.walle.center.board.SendGroupMessageDTO;
import com.ngnis.walle.common.HttpContext;
import com.ngnis.walle.common.bean.BeanMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author houyi.wh
 * @since 2018-09-14
 */
@RestController // @Controller and @ResponseBody
public class BaseController {

    @RequestMapping("/")
    public ModelAndView index() {
        return new ModelAndView("redirect:index.html");
    }

    QueryDTO newAccountQueryDTO() {
        return newAccountQueryDTO(null);
    }

    QueryDTO newAccountQueryDTO(QueryDTO queryDTO) {
        QueryDTO newAccountQuery = queryDTO == null ? new QueryDTO() : BeanMapper.map(queryDTO, QueryDTO.class);
        newAccountQuery.setUserId(HttpContext.currentContext().getUserId());
        return newAccountQuery;
    }

    UpdatePwdDTO newUpdatePwdDTO(UpdatePwdDTO updatePwdDTO) {
        UpdatePwdDTO newUpdatePwdDTO = BeanMapper.map(updatePwdDTO, UpdatePwdDTO.class);
        newUpdatePwdDTO.setUserId(HttpContext.currentContext().getUserId());
        return newUpdatePwdDTO;
    }

    LogoutDTO newLogoutDTO() {
        return LogoutDTO.builder()
                .userId(HttpContext.currentContext().getUserId())
                .build();
    }

    GroupBoardDTO newBoardDTO(String boardCode) {
        return newBoardDTO(
                GroupBoardDTO.builder()
                        .boardCode(boardCode)
                        .build()
        );
    }

    GroupBoardDTO newBoardDTO(GroupBoardDTO board) {
        GroupBoardDTO newBoard = BeanMapper.map(board, GroupBoardDTO.class);
        newBoard.setUserId(HttpContext.currentContext().getUserId());
        return newBoard;
    }

    GroupBoardQueryDTO newBoardQueryDTO(GroupBoardQueryDTO queryDTO) {
        GroupBoardQueryDTO newBoardQuery = BeanMapper.map(queryDTO, GroupBoardQueryDTO.class);
        newBoardQuery.setUserId(HttpContext.currentContext().getUserId());
        return newBoardQuery;
    }

    SendGroupMessageDTO newGroupMessageDTO(SendGroupMessageDTO messageDTO) {
        SendGroupMessageDTO newMessageDTO = BeanMapper.map(messageDTO, SendGroupMessageDTO.class);
        newMessageDTO.setUserId(HttpContext.currentContext().getUserId());
        return newMessageDTO;
    }

}
