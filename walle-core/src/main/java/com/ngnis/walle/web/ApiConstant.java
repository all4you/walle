package com.ngnis.walle.web;

/**
 * @author houyi.wh
 * @since 2018-09-14
 */
public interface ApiConstant {

    interface Urls {
        String ROOT = "/walle";

        String SEND_MESSAGE = "/walle/message/send";

        String GROUP_BOARD = "/walle/groupBoard";
        String GET_BOARDS_CNT = "/cnt";
        String GET_BOARDS = "/list";
        String CREATE_BOARD = "/create";
        String MODIFY_BOARD = "/modify";
        String REMOVE_BOARD = "/remove";
        String FIND_BOARD = "/{boardCode:.+}";

        String USER = "/walle/user";
        String USER_REGISTER = "/register";
        String USER_LOGIN = "/login";
        String USER_UPDATE_PASSWORD = "/updpwd";
        String GET_USER_INFO = "/info";
        String GET_USER_SK = "/sk";
        String USER_LOGOUT = "/logout";

        String API = "/walle/api";
        String SEND_GROUP_MSG = "/groupMsg/send";

    }

}
