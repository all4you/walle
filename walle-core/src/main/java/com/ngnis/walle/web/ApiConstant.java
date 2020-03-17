package com.ngnis.walle.web;

/**
 * @author houyi.wh
 * @since 2018-09-14
 */
public interface ApiConstant {

    interface Urls {
        String ROOT = "/walle";

        String SEND_MESSAGE = "/walle/message/send";

        String GET_BOARDS = "/walle/boards";
        String CREATE_BOARD = "/walle/board/create";
        String MODIFY_BOARD = "/walle/board/modify";
        String REMOVE_BOARD = "/walle/board/remove";
        String FIND_BOARD = "/walle/board/{boardCode:.+}";

        String USER = "/walle/user";
        String USER_REGISTER = "/register";
        String USER_LOGIN = "/login";
        String USER_UPDATE_PASSWORD = "/updpwd";
        String GET_USER_INFO = "/info";
        String GET_USER_SK = "/sk";
        String USER_LOGOUT = "/logout";

    }

}
